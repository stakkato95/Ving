package com.github.stakkato95.ving.processor;

/**
 * Created by Artyom on 21.11.2014.
 */
public interface Processor <Input,Output> {

    Output process(Input source) throws Exception;

}