package com.dm.disruptor;

import com.dm.core.SqlParserTask;
import java.util.List;

/**
 * @author zy
 */
public class SeriesData {

    private List<SqlParserTask> tasks;

    public SeriesData() {
    }

    public SeriesData(List<SqlParserTask> tasks) {
        this.tasks = tasks;
    }

    public List<SqlParserTask> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "SeriesData{" +
                "tasks=" + tasks +
                '}';
    }
}