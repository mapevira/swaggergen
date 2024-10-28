package com.mapfre.tron.api.swaggergen.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by jt, Spring Framework Guru.
 *
 * @author architecture - rperezv
 * @version 25/10/2024 - 12:51
 * @since jdk 1.17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Property {

    String name;
    String description;

}
