package vip.testops.manager.entity.vto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BugVTO {
    private String date;
    private int bugs;
}
