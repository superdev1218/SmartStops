package com.creativeinfoway.smartstops.app.android.navigation.ui.v5;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public  class GetAllSubCategoryWaypoint  implements Parcelable{


    /**
     * status : 1
     * data : [{"id":"177","cat_id":"139","sub_cat_id":"421, 422","latitude":"22 15 54.7 N","longitude":"70 47 49.0 E","latitude_decimal":"22.2651944444","longitude_decimal":"70.7969444444","name":"Kalawad Road Dominoz","waypoint_id":"20592","address":"abc","additional_info":"","country":"Canada","email":"abc@gmail.com","phone_number":"13123123123","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/057fb289b807377e6f60c5470eaebcb3.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/smartstopslogo.png","sub_cat_array":[{"id":"421","name":"Domino"},{"id":"422","name":"Pizza Hut"}]},{"id":"178","cat_id":"139","sub_cat_id":"","latitude":"22 15 54.7 N","longitude":"70 47 49.0 E","latitude_decimal":"22.2651944444","longitude_decimal":"70.7969444444","name":"Tagore Road Dominoz","waypoint_id":"20592","address":"abc","additional_info":"","country":"Canada","email":"abc@gmail.com","phone_number":"13123123123","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/ad5b59fe0ef3510a00327d8a26c9e517.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/smartstopslogo.png","sub_cat_array":[]},{"id":"179","cat_id":"139","sub_cat_id":"422","latitude":"22 15 53.9 N","longitude":"70 47 53.8 E","latitude_decimal":"22.2649722222","longitude_decimal":"70.7982777778","name":"150Ft road pissa sut","waypoint_id":"53092","address":"xyz","additional_info":"","country":"Canada","email":"xyz@gmail.com","phone_number":"12312312313","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/d7e9e3697c02d5875e277188fbee890e.png","waypoint_image":"http://canadasmartstops.com/smartstop/admin/image/smartstopslogo.png","sub_cat_array":[{"id":"422","name":"Pizza Hut"}]}]
     */

    //{"status":"0","msg":"Location not found."}

    private String status;
    private String msg;
    private List<DataBean> data;

    protected GetAllSubCategoryWaypoint(Parcel in) {
        status = in.readString();
        msg = in.readString();
        data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Creator<GetAllSubCategoryWaypoint> CREATOR = new Creator<GetAllSubCategoryWaypoint>() {
        @Override
        public GetAllSubCategoryWaypoint createFromParcel(Parcel in) {
            return new GetAllSubCategoryWaypoint(in);
        }

        @Override
        public GetAllSubCategoryWaypoint[] newArray(int size) {
            return new GetAllSubCategoryWaypoint[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(msg);
        dest.writeTypedList(data);
    }

    public static class DataBean implements Parcelable {

        /**
         * id : 177
         * cat_id : 139
         * sub_cat_id : 421, 422
         * latitude : 22 15 54.7 N
         * longitude : 70 47 49.0 E
         * latitude_decimal : 22.2651944444
         * longitude_decimal : 70.7969444444
         * name : Kalawad Road Dominoz
         * waypoint_id : 20592
         * address : abc
         * additional_info :
         * country : Canada
         * email : abc@gmail.com
         * phone_number : 13123123123
         * waypoint_icon_image : http://canadasmartstops.com/smartstop/admin/image/057fb289b807377e6f60c5470eaebcb3.png
         * waypoint_image : http://canadasmartstops.com/smartstop/admin/image/smartstopslogo.png
         * sub_cat_array : [{"id":"421","name":"Domino"},{"id":"422","name":"Pizza Hut"}]
         * "option_image_1":"","option_image_2":"","option_image_3":""
         */

        private String id;
        private String cat_id,cat_name;
        private String sub_cat_id;
        private String latitude;
        private String longitude;
        private String latitude_decimal;
        private String longitude_decimal;
        private String name;
        private String waypoint_id;
        private String address;
        private String additional_info;
        private String country;
        private String email;
        private String phone_number;
        private String waypoint_icon_image;
        private String waypoint_image;
        private List<SubCatArrayBean> sub_cat_array;
        private String new_id;
        private String mapbox_icon_visibility;
        private Bitmap bitmap;
        private String option_image_1;
        private String option_image_2;
        private String option_image_3;

        public String getMapbox_icon_visibility() {
            if(mapbox_icon_visibility == null){
                return "true";
            } else {
                return mapbox_icon_visibility;
            }
        }

        public void setMapbox_icon_visibility(String mapbox_icon_visibility) {
            this.mapbox_icon_visibility = mapbox_icon_visibility;
        }

        public String getNew_id() {
            return new_id;
        }

        public void setNew_id(String new_id) {
            this.new_id = new_id;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public DataBean() {
        }

        public DataBean(Parcel in) {
            id = in.readString();
            cat_id = in.readString();
            cat_name = in.readString();
            sub_cat_id = in.readString();
            latitude = in.readString();
            longitude = in.readString();
            latitude_decimal = in.readString();
            longitude_decimal = in.readString();
            name = in.readString();
            waypoint_id = in.readString();
            address = in.readString();
            additional_info = in.readString();
            country = in.readString();
            email = in.readString();
            phone_number = in.readString();
            waypoint_icon_image = in.readString();
            waypoint_image = in.readString();
            mapbox_icon_visibility = in.readString();
            option_image_1 = in.readString();
            option_image_2 = in.readString();
            option_image_3 = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public String getOption_image_1() {
            return option_image_1;
        }

        public void setOption_image_1(String option_image_1) {
            this.option_image_1 = option_image_1;
        }

        public String getOption_image_2() {
            return option_image_2;
        }

        public void setOption_image_2(String option_image_2) {
            this.option_image_2 = option_image_2;
        }

        public String getOption_image_3() {
            return option_image_3;
        }

        public void setOption_image_3(String option_image_3) {
            this.option_image_3 = option_image_3;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getSub_cat_id() {
            return sub_cat_id;
        }

        public void setSub_cat_id(String sub_cat_id) {
            this.sub_cat_id = sub_cat_id;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude_decimal() {
            return latitude_decimal;
        }

        public void setLatitude_decimal(String latitude_decimal) {
            this.latitude_decimal = latitude_decimal;
        }

        public String getLongitude_decimal() {
            return longitude_decimal;
        }

        public void setLongitude_decimal(String longitude_decimal) {
            this.longitude_decimal = longitude_decimal;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getWaypoint_id() {
            return waypoint_id;
        }

        public void setWaypoint_id(String waypoint_id) {
            this.waypoint_id = waypoint_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAdditional_info() {
            return additional_info;
        }

        public void setAdditional_info(String additional_info) {
            this.additional_info = additional_info;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getWaypoint_icon_image() {
            return waypoint_icon_image;
        }

        public void setWaypoint_icon_image(String waypoint_icon_image) {
            this.waypoint_icon_image = waypoint_icon_image;
        }

        public String getWaypoint_image() {
            return waypoint_image;
        }

        public void setWaypoint_image(String waypoint_image) {
            this.waypoint_image = waypoint_image;
        }

        public List<SubCatArrayBean> getSub_cat_array() {
            return sub_cat_array;
        }

        public void setSub_cat_array(List<SubCatArrayBean> sub_cat_array) {
            this.sub_cat_array = sub_cat_array;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(cat_id);
            dest.writeString(cat_name);
            dest.writeString(sub_cat_id);
            dest.writeString(latitude);
            dest.writeString(longitude);
            dest.writeString(latitude_decimal);
            dest.writeString(longitude_decimal);
            dest.writeString(name);
            dest.writeString(waypoint_id);
            dest.writeString(address);
            dest.writeString(additional_info);
            dest.writeString(country);
            dest.writeString(email);
            dest.writeString(phone_number);
            dest.writeString(waypoint_icon_image);
            dest.writeString(waypoint_image);
            dest.writeString(mapbox_icon_visibility);
            dest.writeString(option_image_1);
            dest.writeString(option_image_2);
            dest.writeString(option_image_3);
        }

        public static class SubCatArrayBean {
            /**
             * id : 421
             * name : Domino
             */

            private String id;
            private String name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
