package org.Application.command.model.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleInterviewRequest {

    @NotBlank(message = "Người phỏng vấn không được để trống")
    private String interviewerId;

    @NotBlank(message = "Tiêu đề buổi phỏng vấn không được để trống")
    private String title;

    @NotNull(message = "Ngày phỏng vấn không được để trống")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate interviewDate;

    @NotNull(message = "Thời gian bắt đầu không được để trống")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startTime;

    @NotNull(message = "Thời gian kết thúc không được để trống")
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;

    @NotBlank(message = "Địa điểm phỏng vấn không được để trống")
    private String location;

    public static class LocalTimeDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<LocalTime> {
        @Override
        public LocalTime deserialize(com.fasterxml.jackson.core.JsonParser parser, com.fasterxml.jackson.databind.DeserializationContext context) throws java.io.IOException {
            if (parser.hasToken(com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
                return LocalTime.parse(parser.getText());
            }
            if (parser.hasToken(com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                com.fasterxml.jackson.databind.JsonNode node = parser.readValueAsTree();
                int hour = node.has("hour") ? node.get("hour").asInt() : 0;
                int minute = node.has("minute") ? node.get("minute").asInt() : 0;
                int second = node.has("second") ? node.get("second").asInt() : 0;
                int nano = node.has("nano") ? node.get("nano").asInt() : 0;
                return LocalTime.of(hour, minute, second, nano);
            }
            return null;
        }
    }

    public static class LocalDateDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<LocalDate> {
        @Override
        public LocalDate deserialize(com.fasterxml.jackson.core.JsonParser parser, com.fasterxml.jackson.databind.DeserializationContext context) throws java.io.IOException {
            if (parser.hasToken(com.fasterxml.jackson.core.JsonToken.VALUE_STRING)) {
                return LocalDate.parse(parser.getText());
            }
            if (parser.hasToken(com.fasterxml.jackson.core.JsonToken.START_OBJECT)) {
                com.fasterxml.jackson.databind.JsonNode node = parser.readValueAsTree();
                int year = node.has("year") ? node.get("year").asInt() : 1970;
                int month = node.has("monthValue") ? node.get("monthValue").asInt() : (node.has("month") ? node.get("month").asInt() : 1);
                int day = node.has("dayOfMonth") ? node.get("dayOfMonth").asInt() : (node.has("day") ? node.get("day").asInt() : 1);
                return LocalDate.of(year, month, day);
            }
            return null;
        }
    }
}
