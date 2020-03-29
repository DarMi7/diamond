package com.dm.sample.entity.pojodo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zy
 * @create 2019-03-17 22:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private int id;
    private String username;
    private String password;

}
