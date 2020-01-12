package com.wpam.spenpresentationcontrol.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SocketAddress {
    @PrimaryKey
    public int id;

    public String address;

    public Integer port;

    public SocketAddress(String address, Integer port) {
        this.id = 0;
        this.address = address;
        this.port = port;
    }
}
