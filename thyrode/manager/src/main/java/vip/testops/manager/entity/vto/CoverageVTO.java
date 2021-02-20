package vip.testops.manager.entity.vto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoverageVTO {
    private String date;
    private Double coverage;
}
