package com.dasmo.drs_api.mapper;

import java.util.List;

public interface BaseMapper<E, D> {

	D toDto(E entity);

	List<D> toDtoList(List<E> entity);
}
