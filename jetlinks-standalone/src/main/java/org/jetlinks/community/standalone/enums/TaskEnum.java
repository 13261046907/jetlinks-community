package org.jetlinks.community.standalone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetlinks.community.standalone.exception.BizException;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
public enum TaskEnum {
    TASK1("2", "0 0/2 * * * ?"),
    TASK2("5", "0 0/5 * * * ?"),
    TASK3("10", "0 0/10 * * * ?"),
    TASK4("30", "0 0/30 * * * ?"),
    TASK5("60", "0 0 */1 * * ?"),
    TASK6("1440", "0 0 1 * * ?")
    ;

    @Getter
    private final String value;

    @Getter
    private final String key;


    public static TaskEnum getTaskKey(String value) {
        Optional<TaskEnum> optional = Arrays.stream(values())
                .filter(instance -> instance.value.contains(value)).limit(1)
                .findFirst();

        if (optional.isPresent()) {
         return optional.get();
        }
        throw new BizException(500,"未知平台: " + value);
    }

}
