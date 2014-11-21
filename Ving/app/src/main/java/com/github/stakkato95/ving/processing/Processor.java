package com.github.stakkato95.ving.processing;

/**
 * Created by Artyom on 21.11.2014.
 */
public interface Processor <ProcessingResult, Source> {

    ProcessingResult process(Source source) throws Exception;

}