package com.creativeinfoway.smartstops.app.ui.models;

import java.util.List;

public class LegendWayPointModel  {


    /**
     * status : 1
     * data : [{"id":"21","name":"A&W","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/ed8902677e0e13253ca46e3b57339307.gif"},{"id":"56","name":"Abbotsford International Airport","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/0bb53e4c4c7646c04a19d59bd75111e4.png"},{"id":"34","name":"Attraction","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/27be2142e9a05ebc7d1e5f834578f2b4.png"},{"id":"63","name":"Bakery","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/27bd85e28167b8f4452bb82ffe1f9850.png"},{"id":"78","name":"Bed & Breakfast","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/de3f03fb90f601ba55549e95b439ffc4.png"},{"id":"40","name":"Burger King","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/9a23665e00609c2955b525f3a9001da6.png"},{"id":"114","name":"Carpenter Lake BC.","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1839948947cca33bc8f166ddc5d62b40.png"},{"id":"58","name":"Chilliwack Airport","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b3f336f96525e8c01c8fc134384913a0.png"},{"id":"119","name":"Demo_WaypointName_Cartwheel","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/7ad66013164e876aa11d03aacd2ea32c.png"},{"id":"118","name":"endlessnhdkdbd","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/9742be9fa3e6d65f59f66573024c6916.png"},{"id":"29","name":"Free RV or Tent Camping","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/469dc8781839a830761d128e02d310d3.png"},{"id":"110","name":"Free RV or Tent Camping","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/41ad12c63459ff234666e6d30f456937.png"},{"id":"67","name":"Fuel","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/9807b65b975df320f3df6e0a452a5ddb.png"},{"id":"76","name":"Fuel","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b1146f0f42e6604cc8da5e9337c7abc4.png"},{"id":"77","name":"Golf","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1e1dd01df0f1bde681e4038ed5e0324d.png"},{"id":"122","name":"H J Doshi Hospital","additional_info":"Testing Waypoint","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/16560c43b2893fbaba6f560f5f2ff049.png"},{"id":"65","name":"Hospital","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/f966c9bbbeb8b0a64977eae438c9a678.png"},{"id":"53","name":"Johnson Lake","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/59c1a47dffa51342b6d3952c88b73a6d.png"},{"id":"116","name":"Lake Pavilion BC.","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/38b45d80ab4b2d3f6c3df2254176d85b.png"},{"id":"115","name":"Lake Seton BC.","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/633b1a23c1e8a30b07961c21dab7843b.png"},{"id":"15","name":"Library ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/5c64d1e200ae2136420472d4934f5745.png"},{"id":"54","name":"Lillooet Lake","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/eeec96a4545daaa5e0b85e7a3796824d.png"},{"id":"74","name":"Market Square","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/a263703c7f0f4d6f3ac486f20509ac9d.png"},{"id":"64","name":"Medical Walk in Clinic","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b3d59920f6472fd0c8fab5c47ca053d3.png"},{"id":"80","name":"Narrow Bridge ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/fb321e0bba5ebd8c2e33f50f5a3e30da.png"},{"id":"28","name":"Picnic Area","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/cd2bc7030edb12f734f094f244bbced0.png"},{"id":"68","name":"Picnic Area","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/ab8bba14c6e5bf0d1b825b3456551287.png"},{"id":"23","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/7b765376e9e244a4ca62306e9b5a20a8.png"},{"id":"24","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/84291c115b81328760d741bd699b2d28.png"},{"id":"69","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/a10adb6af0f1115f9790c4883672870f.png"},{"id":"79","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/34faf185dfd592b349f4bf2fe9632038.png"},{"id":"93","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/9c7c4d3d248cac138b10ee78f3f6f950.png"},{"id":"95","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/23885bd8d2fd104449de8bff9b09f483.png"},{"id":"96","name":"Point of Interest","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/498b4977452506ca5a2150514b082222.png"},{"id":"70","name":"Police ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/5326800e45be408728bd3d4204dfd7ef.png"},{"id":"72","name":"Police","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/22c85b56f5acb0a5ba5f8388c0d470e1.png"},{"id":"22","name":"Post Office","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/c7230a134a3f57e75a7b9e4626fbe031.png"},{"id":"71","name":"Pub or Bar","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/e42362e63eaeeaa95561eb1ede5d7915.png"},{"id":"81","name":"Pullout Large","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/423379119993eb804e677a5d84b3fcae.png"},{"id":"82","name":"Pullout Large","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/c679c687c3aed573faf1c4800a86d2d3.png"},{"id":"90","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/d67faf02d750ba4d747d654cd3db381c.png"},{"id":"91","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/295fc0cd4fc28f149b6018f87a94d14e.png"},{"id":"92","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/d919f9f448273ec522ed432a3d1900a5.png"},{"id":"98","name":"Pullout Large","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/3f21c421cab10b0b1e8b5dc0a9898bc2.png"},{"id":"99","name":"Pullout Large","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/51490d5bd1578f8631cd0d3d5f670c16.png"},{"id":"103","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/8c2eff06096a3e5c6925c3e3942022da.png"},{"id":"109","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1c6669db1078bbcbdd8629bfbe7971d0.png"},{"id":"112","name":"Pullout Large ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/3ec8db5a051c5a523bebc9eadc59a6f5.png"},{"id":"83","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/a397300cecdfa43b124346fa7cf8dc51.png"},{"id":"87","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/7691fd4501b2e30cd6187d82a35fb8c7.png"},{"id":"88","name":"Pullout Small ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/a69d87e96d4f3f4addfc9aa4447197af.png"},{"id":"94","name":"Pullout Small ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/36af3683196363c9ea8af288d51ceba6.png"},{"id":"100","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/c966f3f5059fab978637a4df9eadcd1c.png"},{"id":"102","name":"Pullout Small ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/708b8c0b265d69a1bd69552470578f52.png"},{"id":"104","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/ad67cf20e1225643f230bd8f1c6db5aa.png"},{"id":"105","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/18969b936b40664e450862afca53072a.png"},{"id":"108","name":"Pullout Small","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/f3d91a433e03bcabb067dcaba96cb1cc.png"},{"id":"42","name":"Rest Area","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b3856921991e9f75f34291c97a5d77c5.png"},{"id":"33","name":"Restaurant - Chinese","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/38ab728725470f3319172186801d4c8c.png"},{"id":"32","name":"Restaurant - Greek","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/33dcb2a4412c4184e45f5fecc4a011c8.png"},{"id":"61","name":"Restaurant Chinese","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1152e31b1e8d262313c2e0b158b7ff2a.png"},{"id":"117","name":"rmc","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/0ea6758705297dfb58127373bc145dd4.png"},{"id":"30","name":"RV Park","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1f9542590c23c510eab40415cb46b4e7.png"},{"id":"84","name":"Seton Dam Campground Lillooet","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/6f2c185893e0722b8bddad8eef4f1e1f.png"},{"id":"31","name":"SmartStop","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/4a9eee88f2751cce324a1ede3b8a6659.png"},{"id":"97","name":"SmartStop","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/3d6d6232326e6e3a7190417d7d4a299d.png"},{"id":"106","name":"SmartStop","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/e6cd146210f892ae12771b5bada24a3e.png"},{"id":"107","name":"SmartStop","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/06daccfdb6af698e5e5d3b5fd742f19a.png"},{"id":"111","name":"SmartStop","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/6ade42de978e35fea286d2733601589c.png"},{"id":"85","name":"Steep Down Hill","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/77ad53df23d8062d6aac21594d8d9abd.png"},{"id":"86","name":"Steep Down Hill","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/efb8ad5db1320d0a167611b866961087.png"},{"id":"101","name":"Steep Down Hill","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/c5c9fa018a8ee898bb72899081315cea.png"},{"id":"113","name":"Steep Down Hill","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/8d2a0eb6662ad7a59c92d982d975e953.png"},{"id":"25","name":"Subway","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/e85e8368a0a9e9b1431d139db49bf1a8.png"},{"id":"75","name":"Subway","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/1f5b4aa558703a7f89e8d14a130c940f.png"},{"id":"73","name":"Tire Sales & Service","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/64c47b26dfb451f69c20f8f044e45e68.png"},{"id":"55","name":"Vancouver International Airport","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b605081c3c80070ea25c6f33444f03c0.png"},{"id":"66","name":"Vet","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/7fac148453ab220d287fde4f28f9b701.png"},{"id":"57","name":"Victoria Airport","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/8c0c72f4d92c1c52e5034e636982f43d.png"},{"id":"89","name":"Viewpoint","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/6d1b9bc5d3f8a503fa3d4a40cb9acb66.png"},{"id":"14","name":"Visitor Info ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/ea56477e8da00001c4fdb8d8d37b43fe.png"},{"id":"44","name":"Walmart","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/f8d25f6fb3055fc17e7c66b52dca5dc1.png"},{"id":"120","name":"Waypoint Name Example","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/b51760c768103ff3e998a83fe996aebd.png"},{"id":"12","name":"Winery ","additional_info":"","waypoint_icon_image":"http://canadasmartstops.com/smartstop/admin/image/a1211d89e7c232c92c7e5c1f3f0282a7.png"}]
     */

    private String status;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;

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

    public static class DataBean {
        /**
         * id : 21
         * name : A&W
         * additional_info :
         * waypoint_icon_image : http://canadasmartstops.com/smartstop/admin/image/ed8902677e0e13253ca46e3b57339307.gif
         */

        private String id;
        private String name;
        private String additional_info;
        private String waypoint_icon_image;

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

        public String getAdditional_info() {
            return additional_info;
        }

        public void setAdditional_info(String additional_info) {
            this.additional_info = additional_info;
        }

        public String getWaypoint_icon_image() {
            return waypoint_icon_image;
        }

        public void setWaypoint_icon_image(String waypoint_icon_image) {
            this.waypoint_icon_image = waypoint_icon_image;
        }
    }
}
