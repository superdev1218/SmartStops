package com.creativeinfoway.smartstops.app.ui.models;

public class GetCurrentCondition {

    /**
     * LocalObservationDateTime : 2019-09-07T11:50:00+05:30
     * EpochTime : 1567837200
     * WeatherText : Mostly cloudy
     * WeatherIcon : 6
     * HasPrecipitation : false
     * PrecipitationType : null
     * IsDayTime : true
     * Temperature : {"Metric":{"Value":30.4,"Unit":"C","UnitType":17},"Imperial":{"Value":87,"Unit":"F","UnitType":18}}
     * MobileLink : http://m.accuweather.com/en/in/rajkot/202440/current-weather/202440?lang=en-us
     * Link : http://www.accuweather.com/en/in/rajkot/202440/current-weather/202440?lang=en-us
     */

    private String LocalObservationDateTime;
    private int EpochTime;
    private String WeatherText;
    private int WeatherIcon;
    private boolean HasPrecipitation;
    private Object PrecipitationType;
    private boolean IsDayTime;
    private TemperatureBean Temperature;
    private String MobileLink;
    private String Link;

    public String getLocalObservationDateTime() {
        return LocalObservationDateTime;
    }

    public void setLocalObservationDateTime(String LocalObservationDateTime) {
        this.LocalObservationDateTime = LocalObservationDateTime;
    }

    public int getEpochTime() {
        return EpochTime;
    }

    public void setEpochTime(int EpochTime) {
        this.EpochTime = EpochTime;
    }

    public String getWeatherText() {
        return WeatherText;
    }

    public void setWeatherText(String WeatherText) {
        this.WeatherText = WeatherText;
    }

    public int getWeatherIcon() {
        return WeatherIcon;
    }

    public void setWeatherIcon(int WeatherIcon) {
        this.WeatherIcon = WeatherIcon;
    }

    public boolean isHasPrecipitation() {
        return HasPrecipitation;
    }

    public void setHasPrecipitation(boolean HasPrecipitation) {
        this.HasPrecipitation = HasPrecipitation;
    }

    public Object getPrecipitationType() {
        return PrecipitationType;
    }

    public void setPrecipitationType(Object PrecipitationType) {
        this.PrecipitationType = PrecipitationType;
    }

    public boolean isIsDayTime() {
        return IsDayTime;
    }

    public void setIsDayTime(boolean IsDayTime) {
        this.IsDayTime = IsDayTime;
    }

    public TemperatureBean getTemperature() {
        return Temperature;
    }

    public void setTemperature(TemperatureBean Temperature) {
        this.Temperature = Temperature;
    }

    public String getMobileLink() {
        return MobileLink;
    }

    public void setMobileLink(String MobileLink) {
        this.MobileLink = MobileLink;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }

    public static class TemperatureBean {
        /**
         * Metric : {"Value":30.4,"Unit":"C","UnitType":17}
         * Imperial : {"Value":87,"Unit":"F","UnitType":18}
         */

        private MetricBean Metric;
        private ImperialBean Imperial;

        public MetricBean getMetric() {
            return Metric;
        }

        public void setMetric(MetricBean Metric) {
            this.Metric = Metric;
        }

        public ImperialBean getImperial() {
            return Imperial;
        }

        public void setImperial(ImperialBean Imperial) {
            this.Imperial = Imperial;
        }

        public static class MetricBean {
            /**
             * Value : 30.4
             * Unit : C
             * UnitType : 17
             */

            private double Value;
            private String Unit;
            private int UnitType;

            public double getValue() {
                return Value;
            }

            public void setValue(double Value) {
                this.Value = Value;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String Unit) {
                this.Unit = Unit;
            }

            public int getUnitType() {
                return UnitType;
            }

            public void setUnitType(int UnitType) {
                this.UnitType = UnitType;
            }
        }

        public static class ImperialBean {
            /**
             * Value : 87.0
             * Unit : F
             * UnitType : 18
             */

            private double Value;
            private String Unit;
            private int UnitType;

            public double getValue() {
                return Value;
            }

            public void setValue(double Value) {
                this.Value = Value;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String Unit) {
                this.Unit = Unit;
            }

            public int getUnitType() {
                return UnitType;
            }

            public void setUnitType(int UnitType) {
                this.UnitType = UnitType;
            }
        }
    }
}
