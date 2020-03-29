package com.dm.disruptor;

import com.dm.constant.DmConstants;
import com.dm.core.NamedThreadFactory;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseQueueHelper<D, E extends ValueWrapper<D>, H extends WorkHandler<E>> {

	/**
	 * 记录所有的队列，系统退出时统一清理资源
	 */
	private List<BaseQueueHelper> queueHelperList = new ArrayList<BaseQueueHelper>();
	/**
	 * Disruptor 对象
	 */
	private Disruptor<E> disruptor;
	/**
	 * RingBuffer
	 */
	private RingBuffer<E> ringBuffer;
	/**
	 * initQueue
	 */
	private List<D> initQueue = new ArrayList();

	/**
	 * 事件工厂
	 *
	 * @return EventFactory
	 */
	protected EventFactory<E> eventFactory() {
		return null;
	}

	/**
	 * 队列大小
	 *
	 * @return 队列长度，必须是2的幂
	 */
	protected int getQueueSize() {
		return 0;
	}

	/**
	 * 初始化
	 */
	public void init() {
		this.disruptor = new Disruptor<E>(eventFactory(), getQueueSize(), new NamedThreadFactory(DmConstants.DIS_THREAD_NAME_FORMAT), ProducerType.SINGLE, getStrategy());
		this.disruptor.setDefaultExceptionHandler(new DisruptorHandlerException());
		this.disruptor.handleEventsWithWorkerPool(new WorkHandler[]{new DispatcherEventHandler()});
		this.ringBuffer = disruptor.start();

		//初始化数据发布
		for (D data : initQueue) {
			ringBuffer.publishEvent(new EventTranslatorOneArg<E, D>() {
				@Override
				public void translateTo(E event, long sequence, D data) {
					event.setValue(data);
				}
			}, data);
		}

		//加入资源清理钩子
		synchronized (queueHelperList) {
			if (queueHelperList.isEmpty()) {
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						for (BaseQueueHelper baseQueueHelper : queueHelperList) {
							baseQueueHelper.shutdown();
						}
					}
				});
			}
			queueHelperList.add(this);
		}
	}

	/**
	 * 如果要改变线程执行优先级，override此策略. YieldingWaitStrategy会提高响应并在闲时占用70%以上CPU，
	 * 慎用SleepingWaitStrategy会降低响应更减少CPU占用，用于日志等场景.
	 *
	 * @return WaitStrategy
	 */
	protected abstract WaitStrategy getStrategy();

	/**
	 * 插入队列消息，支持在对象init前插入队列，则在队列建立时立即发布到队列处理.
	 */
	public synchronized void publishEvent(D data) {
		if (ringBuffer == null) {
			initQueue.add(data);
			return;
		}
		ringBuffer.publishEvent((event, sequence, data1) -> event.setValue(data1), data);
	}

	/**
	 * 关闭队列
	 */
	public void shutdown() {
		disruptor.shutdown();
	}
}