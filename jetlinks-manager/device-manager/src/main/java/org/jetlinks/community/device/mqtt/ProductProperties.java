package org.jetlinks.community.device.mqtt;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductProperties implements Serializable {
    private String id;
    private String name;
}
