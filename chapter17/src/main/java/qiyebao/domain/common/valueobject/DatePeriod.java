package qiyebao.domain.common.valueobject;

import java.time.LocalDate;
import java.util.Objects;

public class DatePeriod {
    final private LocalDate start;
    final private LocalDate end;

    private DatePeriod(LocalDate start, LocalDate end) {
        Objects.requireNonNull(start, "开始日期不能为空");
        Objects.requireNonNull(end, "结束日期不能为空");

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("结束日期不能小于开始日期！");
        }

        this.start = start;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatePeriod period = (DatePeriod) o;
        return Objects.equals(start, period.start) && Objects.equals(end, period.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    public static DatePeriod of(LocalDate start, LocalDate end) {
        return new DatePeriod(start, end);
    }

    public static DatePeriod of(int startYear
        , int startMonth
        , int startDay
        , int endYear
        , int endMonth
        , int endDay
    ) {
        return new DatePeriod(LocalDate.of(startYear, startMonth, startDay)
            , LocalDate.of(endYear, endMonth, endDay));
    }

    //是否与入参时间段重叠
    public boolean isOverlapped(DatePeriod other) {
        Objects.requireNonNull(other, "入参不能为空！");
        return other.start.isBefore(this.end) && other.end.isAfter(this.start);
    }

    //合并两个时间段
    public DatePeriod merge(DatePeriod other) {
        LocalDate newStart = this.start.isBefore(other.start) ? this.start : other.start;
        LocalDate newEnd = this.end.isAfter(other.end) ? this.end : other.end;
        return new DatePeriod(newStart, newEnd);
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return start + " ~ " + end;
    }
}
