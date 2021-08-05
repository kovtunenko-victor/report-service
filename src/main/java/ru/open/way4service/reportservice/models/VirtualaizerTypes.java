package ru.open.way4service.reportservice.models;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "VirtualaizerTypes", description = "VirtualaizerTypes enum")

public enum VirtualaizerTypes {
    NOT_USE, FILE, SWAP, GZIP, SWAP_GZIP
}
