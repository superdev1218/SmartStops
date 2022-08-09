package com.creativeinfoway.smartstops.app.ui.models;

import java.util.List;

public class GetGeoPositionSearch {


    /**
     * Version : 1
     * Key : 202440
     * Type : City
     * Rank : 25
     * LocalizedName : Rajkot
     * EnglishName : Rajkot
     * PrimaryPostalCode :
     * Region : {"ID":"ASI","LocalizedName":"Asia","EnglishName":"Asia"}
     * Country : {"ID":"IN","LocalizedName":"India","EnglishName":"India"}
     * AdministrativeArea : {"ID":"GJ","LocalizedName":"Gujarat","EnglishName":"Gujarat","Level":1,"LocalizedType":"State","EnglishType":"State","CountryID":"IN"}
     * TimeZone : {"Code":"IST","Name":"Asia/Kolkata","GmtOffset":5.5,"IsDaylightSaving":false,"NextOffsetChange":null}
     * GeoPosition : {"Latitude":22.294,"Longitude":70.792,"Elevation":{"Metric":{"Value":130,"Unit":"m","UnitType":5},"Imperial":{"Value":426,"Unit":"ft","UnitType":0}}}
     * IsAlias : false
     * SupplementalAdminAreas : [{"Level":2,"LocalizedName":"Rajkot","EnglishName":"Rajkot"},{"Level":3,"LocalizedName":"Rajkot","EnglishName":"Rajkot"}]
     * DataSets : ["PremiumAirQuality"]
     */

    private int Version;
    private String Key;
    private String Type;
    private int Rank;
    private String LocalizedName;
    private String EnglishName;
    private String PrimaryPostalCode;
    private RegionBean Region;
    private CountryBean Country;
    private AdministrativeAreaBean AdministrativeArea;
    private TimeZoneBean TimeZone;
    private GeoPositionBean GeoPosition;
    private boolean IsAlias;
    private List<SupplementalAdminAreasBean> SupplementalAdminAreas;
    private List<String> DataSets;

    public int getVersion() {
        return Version;
    }

    public void setVersion(int Version) {
        this.Version = Version;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String Key) {
        this.Key = Key;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int Rank) {
        this.Rank = Rank;
    }

    public String getLocalizedName() {
        return LocalizedName;
    }

    public void setLocalizedName(String LocalizedName) {
        this.LocalizedName = LocalizedName;
    }

    public String getEnglishName() {
        return EnglishName;
    }

    public void setEnglishName(String EnglishName) {
        this.EnglishName = EnglishName;
    }

    public String getPrimaryPostalCode() {
        return PrimaryPostalCode;
    }

    public void setPrimaryPostalCode(String PrimaryPostalCode) {
        this.PrimaryPostalCode = PrimaryPostalCode;
    }

    public RegionBean getRegion() {
        return Region;
    }

    public void setRegion(RegionBean Region) {
        this.Region = Region;
    }

    public CountryBean getCountry() {
        return Country;
    }

    public void setCountry(CountryBean Country) {
        this.Country = Country;
    }

    public AdministrativeAreaBean getAdministrativeArea() {
        return AdministrativeArea;
    }

    public void setAdministrativeArea(AdministrativeAreaBean AdministrativeArea) {
        this.AdministrativeArea = AdministrativeArea;
    }

    public TimeZoneBean getTimeZone() {
        return TimeZone;
    }

    public void setTimeZone(TimeZoneBean TimeZone) {
        this.TimeZone = TimeZone;
    }

    public GeoPositionBean getGeoPosition() {
        return GeoPosition;
    }

    public void setGeoPosition(GeoPositionBean GeoPosition) {
        this.GeoPosition = GeoPosition;
    }

    public boolean isIsAlias() {
        return IsAlias;
    }

    public void setIsAlias(boolean IsAlias) {
        this.IsAlias = IsAlias;
    }

    public List<SupplementalAdminAreasBean> getSupplementalAdminAreas() {
        return SupplementalAdminAreas;
    }

    public void setSupplementalAdminAreas(List<SupplementalAdminAreasBean> SupplementalAdminAreas) {
        this.SupplementalAdminAreas = SupplementalAdminAreas;
    }

    public List<String> getDataSets() {
        return DataSets;
    }

    public void setDataSets(List<String> DataSets) {
        this.DataSets = DataSets;
    }

    public static class RegionBean {
        /**
         * ID : ASI
         * LocalizedName : Asia
         * EnglishName : Asia
         */

        private String ID;
        private String LocalizedName;
        private String EnglishName;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getLocalizedName() {
            return LocalizedName;
        }

        public void setLocalizedName(String LocalizedName) {
            this.LocalizedName = LocalizedName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }
    }

    public static class CountryBean {
        /**
         * ID : IN
         * LocalizedName : India
         * EnglishName : India
         */

        private String ID;
        private String LocalizedName;
        private String EnglishName;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getLocalizedName() {
            return LocalizedName;
        }

        public void setLocalizedName(String LocalizedName) {
            this.LocalizedName = LocalizedName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }
    }

    public static class AdministrativeAreaBean {
        /**
         * ID : GJ
         * LocalizedName : Gujarat
         * EnglishName : Gujarat
         * Level : 1
         * LocalizedType : State
         * EnglishType : State
         * CountryID : IN
         */

        private String ID;
        private String LocalizedName;
        private String EnglishName;
        private int Level;
        private String LocalizedType;
        private String EnglishType;
        private String CountryID;

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getLocalizedName() {
            return LocalizedName;
        }

        public void setLocalizedName(String LocalizedName) {
            this.LocalizedName = LocalizedName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }

        public String getLocalizedType() {
            return LocalizedType;
        }

        public void setLocalizedType(String LocalizedType) {
            this.LocalizedType = LocalizedType;
        }

        public String getEnglishType() {
            return EnglishType;
        }

        public void setEnglishType(String EnglishType) {
            this.EnglishType = EnglishType;
        }

        public String getCountryID() {
            return CountryID;
        }

        public void setCountryID(String CountryID) {
            this.CountryID = CountryID;
        }
    }

    public static class TimeZoneBean {
        /**
         * Code : IST
         * Name : Asia/Kolkata
         * GmtOffset : 5.5
         * IsDaylightSaving : false
         * NextOffsetChange : null
         */

        private String Code;
        private String Name;
        private double GmtOffset;
        private boolean IsDaylightSaving;
        private Object NextOffsetChange;

        public String getCode() {
            return Code;
        }

        public void setCode(String Code) {
            this.Code = Code;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public double getGmtOffset() {
            return GmtOffset;
        }

        public void setGmtOffset(double GmtOffset) {
            this.GmtOffset = GmtOffset;
        }

        public boolean isIsDaylightSaving() {
            return IsDaylightSaving;
        }

        public void setIsDaylightSaving(boolean IsDaylightSaving) {
            this.IsDaylightSaving = IsDaylightSaving;
        }

        public Object getNextOffsetChange() {
            return NextOffsetChange;
        }

        public void setNextOffsetChange(Object NextOffsetChange) {
            this.NextOffsetChange = NextOffsetChange;
        }
    }

    public static class GeoPositionBean {
        /**
         * Latitude : 22.294
         * Longitude : 70.792
         * Elevation : {"Metric":{"Value":130,"Unit":"m","UnitType":5},"Imperial":{"Value":426,"Unit":"ft","UnitType":0}}
         */

        private double Latitude;
        private double Longitude;
        private ElevationBean Elevation;

        public double getLatitude() {
            return Latitude;
        }

        public void setLatitude(double Latitude) {
            this.Latitude = Latitude;
        }

        public double getLongitude() {
            return Longitude;
        }

        public void setLongitude(double Longitude) {
            this.Longitude = Longitude;
        }

        public ElevationBean getElevation() {
            return Elevation;
        }

        public void setElevation(ElevationBean Elevation) {
            this.Elevation = Elevation;
        }

        public static class ElevationBean {
            /**
             * Metric : {"Value":130,"Unit":"m","UnitType":5}
             * Imperial : {"Value":426,"Unit":"ft","UnitType":0}
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
                 * Value : 130.0
                 * Unit : m
                 * UnitType : 5
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
                 * Value : 426.0
                 * Unit : ft
                 * UnitType : 0
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

    public static class SupplementalAdminAreasBean {
        /**
         * Level : 2
         * LocalizedName : Rajkot
         * EnglishName : Rajkot
         */

        private int Level;
        private String LocalizedName;
        private String EnglishName;

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }

        public String getLocalizedName() {
            return LocalizedName;
        }

        public void setLocalizedName(String LocalizedName) {
            this.LocalizedName = LocalizedName;
        }

        public String getEnglishName() {
            return EnglishName;
        }

        public void setEnglishName(String EnglishName) {
            this.EnglishName = EnglishName;
        }
    }
}
