package ie.tud.msc.disertation.domain.distributed;

import ie.tud.msc.disertation.domain.Request;
import lombok.Data;

import java.util.List;

@Data
public class DistributedRequest extends Request {
    private int swarmId;
    private int epoch;
    private List<String> baseUrls;
}
