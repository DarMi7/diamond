package com.dm.disruptor;

import com.dm.constant.DmConstants;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;

/**
 * @author zy
 */
public  class SeriesDataEventQueueHelper extends BaseQueueHelper<SeriesData, SeriesDataEvent, DispatcherEventHandler> {

    public SeriesDataEventQueueHelper() {
        init();
    }

    @Override
    protected int getQueueSize() {
        return DmConstants.RING_BUFFER_SIZE;
    }

    @Override
    protected EventFactory eventFactory() {
        return new EventFactory();
    }

    @Override
    protected WaitStrategy getStrategy() {
        return new BlockingWaitStrategy();
    }

    public static SeriesDataEventQueueHelper Instance() {
        return SeriesDataEventQueueHelperInstance.Instance;
    }

    public static class SeriesDataEventQueueHelperInstance {
        private static SeriesDataEventQueueHelper Instance;
        static {
            Instance = new  SeriesDataEventQueueHelper();
        }
    }
}