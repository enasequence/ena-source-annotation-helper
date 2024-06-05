package uk.ac.ebi.ena.annotation.helper.health;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.StatusAggregator;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * API Health Aggregator to aggregate the health results
 */
@Component
public class ApiHealthAggregator implements StatusAggregator {

    private final HttpServletRequest httpServletRequest;

    public ApiHealthAggregator(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public Status getAggregateStatus(Set<Status> statuses) {
        Set<Status> details = new HashSet<>();
        if (StringUtils.isNotBlank(httpServletRequest.getParameter("client"))
                && httpServletRequest.getParameter("client").equals("k8s")) {
            details.add(Status.UP);
        } else {
            // aggregate all healths
            details.addAll(statuses);
        }
        Optional<Status> any = details.stream().filter(status -> Status.DOWN.getCode().equals(status.getCode())).findAny();
        if (any.isPresent()) {
            return Status.DOWN;
        }
        return Status.UP;
    }
}
