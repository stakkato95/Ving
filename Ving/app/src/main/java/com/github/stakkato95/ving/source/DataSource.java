package com.github.stakkato95.ving.source;

/**
 * Created by Artyom on 19.11.2014.
 */
public interface DataSource <Input,Output> {

    Output getResult(Input params) throws Exception;

}