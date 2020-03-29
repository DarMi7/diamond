package com.dm.disruptor;

public class EventFactory implements com.lmax.disruptor.EventFactory<SeriesDataEvent> {


    @Override
    public SeriesDataEvent newInstance() {
        return new SeriesDataEvent();
    }
}