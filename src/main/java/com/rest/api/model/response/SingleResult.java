package com.rest.api.model.response;

import lombok.Getter;
import lombok.Setter;
/*
 * 결과 Model
 * 결과가 단건인 경우
 *
 * */
@Getter
@Setter
public class SingleResult<T> extends CommonResult { //CommonResult 상속
    private T data;
}