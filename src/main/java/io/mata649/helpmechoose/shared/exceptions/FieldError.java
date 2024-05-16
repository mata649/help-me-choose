package io.mata649.helpmechoose.shared.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldError {
    private String field;
    private String error;
}
