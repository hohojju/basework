package com.rest.api.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
/*
 * 결과 Model
 * 결과가 다건인 경우
 *
 * */
@Getter
@Setter
public class ListResult<T> extends CommonResult {
    private List<T> list;
}