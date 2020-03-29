package com.dm.disruptor;

import com.dm.core.Pipeline;
import com.dm.core.PipelineContext;
import com.dm.core.SqlParserTask;
import com.lmax.disruptor.WorkHandler;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author zy
 */
public class DispatcherEventHandler implements WorkHandler<SeriesDataEvent> {

    private Logger logger = LoggerFactory.getLogger(DispatcherEventHandler.class);

    @Override
    public void onEvent(SeriesDataEvent event)  {
        List<SqlParserTask> tasks = event.getValue().getTasks();
        if (event.getValue() == null || StringUtils.isEmpty(tasks)) {
            logger.warn("Receiver series data is empty!");
        }
        logger.info("Receiver series data is :" + Arrays.toString(tasks.toArray()));
        Pipeline pipeline = PipelineContext.getPipeline();
        tasks.stream().forEach(e -> {
            if (e != null) {
                pipeline.process(e);
            }
        });
    }

/*    public int hash(int parallelSize, long id){
        int iid = Integer.parseInt(String.valueOf(id));
        int n = parallelSize - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        n = (n < 0) ? 1 : (n >= DmConstants.MAXIMUM_CAPACITY) ?  DmConstants.MAXIMUM_CAPACITY : n + 1;
        return (n - 1 ) & iid;
    }*/
}