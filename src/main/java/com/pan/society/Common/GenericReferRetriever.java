package com.pan.society.Common;

import com.yonyou.ocm.common.service.BaseService;
import com.yonyou.ocm.common.service.dto.BaseDto;
import com.yonyou.ocm.common.service.dto.ReferDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用参照获取工具类。
 *
 * @author wangruiv
 * @date 2018-06-08 11:45:34
 */
public class GenericReferRetriever<TDto extends BaseDto, TService extends BaseService> {
    private final Class<TDto> dtoClass;

    private final TService service;

    public GenericReferRetriever(Class<TDto> dtoClass, TService service) {
        this.dtoClass = dtoClass;
        this.service = service;
    }

    /**
     * 获取参照数据列表。
     *
     * @param searchParams 查询参数。
     * @return 参照数据列表。
     */
    public List<ReferDto> retrieveReferDtoList(Map<String, Object> searchParams) {
        List<TDto> baseDtos = service.findAll(searchParams, (Sort) null);
        return baseDtos.stream().map(ReferDto :: of).collect(Collectors.toList());
    }

    /**
     * 获取参照数据分页列表。
     *
     * @param searchParams 查询参数。
     * @param pageable 分页参数。
     * @return 参照数据分页列表。
     */
    public Page<ReferDto> retrieveReferDtoPage(Map<String, Object> searchParams, Pageable pageable) {
        Page<TDto> baseDtos = (Page<TDto>) service.findAll(searchParams, pageable);
        return baseDtos.map(ReferDto :: of);
    }
}
