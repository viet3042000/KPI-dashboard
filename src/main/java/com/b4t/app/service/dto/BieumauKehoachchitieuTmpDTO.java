package com.b4t.app.service.dto;

import com.b4t.app.commons.DateTypeValidate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.b4t.app.domain.BieumauKehoachchitieu} entity.
 */

public class BieumauKehoachchitieuTmpDTO extends BieumauKehoachchitieuDTO implements Serializable {
   private List<String> kpiCodes;

    public List<String> getKpiCodes() {
        return kpiCodes;
    }

    public void setKpiCodes(List<String> kpiCodes) {
        this.kpiCodes = kpiCodes;
    }
}
