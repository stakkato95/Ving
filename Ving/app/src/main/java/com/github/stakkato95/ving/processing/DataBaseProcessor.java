package com.github.stakkato95.ving.processing;

/**
 * Created by Artyom on 17.01.2015.
 */
public interface DatabaseProcessor<Input> {

    void process(Input input) throws Exception;

}
