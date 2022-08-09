package com.creativeinfoway.smartstops.app.ui.models;

import java.util.List;

public class GetPostalCode {
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * status : 1
     * data : [{"zip_code":"J3B 6E6"},{"zip_code":"T2V 2G3"},{"zip_code":"V2B 4V7"},{"zip_code":"E7E 1X4"},{"zip_code":"S4N 1Y2"},{"zip_code":"L9A 1S7"},{"zip_code":"J5M 1H1"},{"zip_code":"V2J 4M8"},{"zip_code":"G4T 1P4"},{"zip_code":"T2K 2N3"},{"zip_code":"J6Z 3B6"},{"zip_code":"G5A 1A3"},{"zip_code":"T2K 5G7"},{"zip_code":"L3P 7P8"},{"zip_code":"J2S 6Z9"},{"zip_code":"L3Y 3H8"},{"zip_code":"E5N 4Z8"},{"zip_code":"M8V 2K6"},{"zip_code":"V4M 2T3"},{"zip_code":"G4R 1J9"},{"zip_code":"T2V 1N9"},{"zip_code":"H8S 2T1"},{"zip_code":"N5H 3A7"},{"zip_code":"K7M 4L9"},{"zip_code":"E9B 1C1"},{"zip_code":"M4Y 2C3"},{"zip_code":"V3X 2H6"},{"zip_code":"T5X 4V7"},{"zip_code":"V1L 2S2"},{"zip_code":"T2E 9C6"},{"zip_code":"L4K 2P4"},{"zip_code":"T2C 0N8"},{"zip_code":"E1G 5M3"},{"zip_code":"T3J 3K2"},{"zip_code":"T3E 7P2"},{"zip_code":"T5E 0T5"},{"zip_code":"K7M 6L7"},{"zip_code":"T2W 4Z4"},{"zip_code":"P6B 6A6"},{"zip_code":"K1C 6Z3"},{"zip_code":"H4B 2R2"},{"zip_code":"L3M 2N6"},{"zip_code":"T5L 1L1"},{"zip_code":"K1C 7G7"},{"zip_code":"T1W 3L6"},{"zip_code":"V6J 1C9"},{"zip_code":"H4L 2V6"},{"zip_code":"N1R 6Y5"},{"zip_code":"E3L 3Y3"},{"zip_code":"V6V 2H2"},{"zip_code":"V9W 2J5"},{"zip_code":"L1Z 1C4"},{"zip_code":"M3J 3G2"},{"zip_code":"L7N 2N7"},{"zip_code":"T2T 4T5"},{"zip_code":"V9W 5H8"},{"zip_code":"E7C 1L8"},{"zip_code":"L9S 4L5"},{"zip_code":"K7C 3M6"},{"zip_code":"J3H 0A8"},{"zip_code":"J1H 0H2"},{"zip_code":"J4Y 1P5"},{"zip_code":"E5N 5Z8"},{"zip_code":"T2K 5X1"},{"zip_code":"B0W 0E3"},{"zip_code":"H4A 3C1"},{"zip_code":"E1B 3W4"},{"zip_code":"J3Y 8J2"},{"zip_code":"J7L 2K3"},{"zip_code":"T0B 0G6"},{"zip_code":"S4N 1H4"},{"zip_code":"V3K 3Y9"},{"zip_code":"M5G 2H5"},{"zip_code":"M4C 5L3"},{"zip_code":"N8X 4J5"},{"zip_code":"E5E 1W1"},{"zip_code":"L3P 4A6"},{"zip_code":"K9K 1N1"},{"zip_code":"L4R 2X9"},{"zip_code":"K7G 2G3"},{"zip_code":"G1G 2P6"},{"zip_code":"T2S 0A3"},{"zip_code":"H7P 2N6"},{"zip_code":"E7M 2W6"},{"zip_code":"N9H 0H7"},{"zip_code":"M1G 2H8"},{"zip_code":"J8X 0B9"},{"zip_code":"M5M 1T2"},{"zip_code":"V4B 4A9"},{"zip_code":"L2T 4A3"},{"zip_code":"V2M 6G8"},{"zip_code":"J4B 6G8"},{"zip_code":"N6K 3E8"},{"zip_code":"G3K 2K6"},{"zip_code":"H1K 3W6"},{"zip_code":"L2H 0K9"},{"zip_code":"L4L 8Y6"},{"zip_code":"M5W 5B6"},{"zip_code":"L1A 1M6"},{"zip_code":"T6K 4B2"},{"zip_code":"T9K 1X3"},{"zip_code":"E4M 2N4"},{"zip_code":"J6S 4Z4"},{"zip_code":"S7L 7H5"},{"zip_code":"T1B 3L1"},{"zip_code":"L5J 1V8"},{"zip_code":"L1G 4J8"},{"zip_code":"G5Y 3T9"},{"zip_code":"J0X 1J0"},{"zip_code":"P4N 2M8"},{"zip_code":"R3K 1V4"},{"zip_code":"T6B 0X9"},{"zip_code":"P1B 8C8"},{"zip_code":"B4V 0Y6"},{"zip_code":"E4E 1P8"},{"zip_code":"V3A 4H2"},{"zip_code":"M1R 1X3"},{"zip_code":"G5V 1N1"},{"zip_code":"P7E 2K3"},{"zip_code":"L1V 3E5"},{"zip_code":"V3K 3Y8"},{"zip_code":"K7M 8X6"},{"zip_code":"N8T 3B6"},{"zip_code":"B9A 2W4"},{"zip_code":"J2K 4K2"},{"zip_code":"L4R 1B7"},{"zip_code":"M6B 3P4"},{"zip_code":"G9H 1V4"},{"zip_code":"G6E 2V8"},{"zip_code":"B2H 4J6"},{"zip_code":"J5T 2J7"},{"zip_code":"N7G 3C8"},{"zip_code":"G8Y 5G4"},{"zip_code":"M3C 2A7"},{"zip_code":"K7S 2T2"},{"zip_code":"L3B 4P8"},{"zip_code":"L9W 0M2"},{"zip_code":"T1S 1R8"},{"zip_code":"H7E 3T6"},{"zip_code":"P5E 1J7"},{"zip_code":"L6W 3V1"},{"zip_code":"P7B 5A5"},{"zip_code":"E2E 2V1"},{"zip_code":"T3K 1R6"},{"zip_code":"V3W 7M2"},{"zip_code":"L3K 3V5"},{"zip_code":"M4C 1J6"},{"zip_code":"T5G 1M7"},{"zip_code":"H4L 2T5"},{"zip_code":"L5J 4L6"},{"zip_code":"T8N 6Y2"},{"zip_code":"T6J 6N4"},{"zip_code":"T3A 0P9"},{"zip_code":"E7M 5R8"},{"zip_code":"V9P 2R9"},{"zip_code":"V8N 1A2"},{"zip_code":"J2S 6G3"},{"zip_code":"N7L 0A1"},{"zip_code":"B5A 1S9"},{"zip_code":"V1T 8X2"},{"zip_code":"S7C 0B1"},{"zip_code":"E7M 5H9"},{"zip_code":"G8Z 1X1"},{"zip_code":"L5B 1S6"},{"zip_code":"T2M 0R4"},{"zip_code":"L3Z 3M7"},{"zip_code":"T6J 5T3"},{"zip_code":"T2B 1L4"},{"zip_code":"N7M 3B4"},{"zip_code":"S7V 1H5"},{"zip_code":"B3Z 2P2"},{"zip_code":"G1L 4N1"},{"zip_code":"N8A 2M6"},{"zip_code":"H7L 2A2"},{"zip_code":"M2M 3G8"},{"zip_code":"V5C 6M1"},{"zip_code":"J6A 1N3"},{"zip_code":"N9E 1K2"},{"zip_code":"V5P 1Y5"},{"zip_code":"L4Y 1E8"},{"zip_code":"M6A 0C4"},{"zip_code":"A8A 2B4"},{"zip_code":"B2A 3N2"},{"zip_code":"G8Y 7L3"},{"zip_code":"S4T 7G4"},{"zip_code":"H4E 3K2"},{"zip_code":"V2L 5P6"},{"zip_code":"L4Z 3S7"},{"zip_code":"V1X 7Y1"},{"zip_code":"L4H 0C9"},{"zip_code":"N1E 1Z1"},{"zip_code":"R4A 0C2"},{"zip_code":"V9Y 8W2"},{"zip_code":"E1B 2J2"},{"zip_code":"L3Y 8E5"},{"zip_code":"T3K 4Y7"},{"zip_code":"V4L 2M8"},{"zip_code":"V3Y 1P8"},{"zip_code":"G6V 5J2"},{"zip_code":"L9W 3S5"},{"zip_code":"T8S 1E2"},{"zip_code":"T8S 1J3"},{"zip_code":"V5X 3C5"},{"zip_code":"G6E 2M9"},{"zip_code":"K9H 1Z3"},{"zip_code":"T2M 2V4"},{"zip_code":"G2B 1Y5"},{"zip_code":"M9M 1K3"},{"zip_code":"R3R 0G7"},{"zip_code":"M6P 1B7"},{"zip_code":"T7Z 1A3"},{"zip_code":"G9P 4X8"},{"zip_code":"G8B 6L6"},{"zip_code":"E1N 1B5"},{"zip_code":"G8T 5Y7"},{"zip_code":"N4K 1Z3"},{"zip_code":"H7A 2K2"},{"zip_code":"L2T 1T4"},{"zip_code":"T2Z 0M7"},{"zip_code":"B3H 3L8"},{"zip_code":"G6Z 7R8"},{"zip_code":"H2Y 1X4"},{"zip_code":"J2K 2W2"},{"zip_code":"N2L 3N9"},{"zip_code":"B1N 3E1"},{"zip_code":"J7V 0V4"},{"zip_code":"E7P 1V6"},{"zip_code":"E4J 2X7"},{"zip_code":"V2M 5M9"},{"zip_code":"J3X 2H1"},{"zip_code":"H7M 2P7"},{"zip_code":"V1Y 3J8"},{"zip_code":"L9A 5B6"},{"zip_code":"L5G 4R8"},{"zip_code":"G1Y 2H3"},{"zip_code":"N4N 2G9"},{"zip_code":"K7R 3K6"},{"zip_code":"M5M 3S3"},{"zip_code":"G1N 4S3"},{"zip_code":"V7A 3B3"},{"zip_code":"P7B 6C3"},{"zip_code":"V4A 6K4"},{"zip_code":"M4V 2P3"},{"zip_code":"N6C 1A4"},{"zip_code":"E3Y 3X4"},{"zip_code":"T2W 2P9"},{"zip_code":"T3E 4R1"},{"zip_code":"V3B 0P9"},{"zip_code":"G6T 1N1"},{"zip_code":"L7N 3K8"},{"zip_code":"07760"},{"zip_code":"13851"},{"zip_code":"31012"},{"zip_code":"36303"},{"zip_code":"75154"},{"zip_code":"24245"},{"zip_code":"31562"},{"zip_code":"60411"},{"zip_code":"55428"},{"zip_code":"80534"},{"zip_code":"92655"},{"zip_code":"76864"},{"zip_code":"95619"},{"zip_code":"97064"},{"zip_code":"39441"},{"zip_code":"47449"},{"zip_code":"56466"},{"zip_code":"18461"},{"zip_code":"94558"},{"zip_code":"17249"},{"zip_code":"44122"},{"zip_code":"37180"},{"zip_code":"15909"},{"zip_code":"74831"},{"zip_code":"13505"},{"zip_code":"28150"},{"zip_code":"69149"},{"zip_code":"95444"},{"zip_code":"61317"},{"zip_code":"16353"},{"zip_code":"15482"},{"zip_code":"35150"},{"zip_code":"59837"},{"zip_code":"40601"},{"zip_code":"32080"},{"zip_code":"39767"},{"zip_code":"20373"},{"zip_code":"64461"},{"zip_code":"17747"},{"zip_code":"21911"},{"zip_code":"48891"},{"zip_code":"77987"},{"zip_code":"07666"},{"zip_code":"49078"},{"zip_code":"06153"},{"zip_code":"62325"},{"zip_code":"37096"},{"zip_code":"66091"},{"zip_code":"97086"},{"zip_code":"12512"},{"zip_code":"17976"},{"zip_code":"48106"},{"zip_code":"77535"},{"zip_code":"60417"},{"zip_code":"33169"},{"zip_code":"37934"},{"zip_code":"91746"},{"zip_code":"31522"},{"zip_code":"62353"},{"zip_code":"30436"},{"zip_code":"43199"},{"zip_code":"91611"},{"zip_code":"81050"},{"zip_code":"06155"},{"zip_code":"17765"},{"zip_code":"27878"},{"zip_code":"18040"},{"zip_code":"52626"},{"zip_code":"48237"},{"zip_code":"47404"},{"zip_code":"50225"},{"zip_code":"33068"},{"zip_code":"40806"},{"zip_code":"18201"},{"zip_code":"60147"},{"zip_code":"50629"},{"zip_code":"66413"},{"zip_code":"50521"},{"zip_code":"15204"},{"zip_code":"75051"},{"zip_code":"59762"},{"zip_code":"17406"},{"zip_code":"65682"},{"zip_code":"98366"},{"zip_code":"71666"},{"zip_code":"33633"},{"zip_code":"42642"},{"zip_code":"54870"},{"zip_code":"38954"},{"zip_code":"10038"},{"zip_code":"91309"},{"zip_code":"32417"},{"zip_code":"48334"},{"zip_code":"72636"},{"zip_code":"40046"},{"zip_code":"26034"},{"zip_code":"54737"},{"zip_code":"73717"},{"zip_code":"76108"},{"zip_code":"36322"},{"zip_code":"07806"},{"zip_code":"44126"},{"zip_code":"26847"},{"zip_code":"88422"},{"zip_code":"15437"},{"zip_code":"78417"},{"zip_code":"25108"},{"zip_code":"64641"},{"zip_code":"58424"},{"zip_code":"86029"},{"zip_code":"32321"},{"zip_code":"97487"},{"zip_code":"30411"},{"zip_code":"36475"},{"zip_code":"45459"},{"zip_code":"05464"},{"zip_code":"31019"},{"zip_code":"71909"},{"zip_code":"04858"},{"zip_code":"17857"},{"zip_code":"63025"},{"zip_code":"35602"},{"zip_code":"45315"},{"zip_code":"58554"},{"zip_code":"60073"},{"zip_code":"95242"},{"zip_code":"91482"},{"zip_code":"40489"},{"zip_code":"97233"},{"zip_code":"07853"},{"zip_code":"31811"},{"zip_code":"31550"},{"zip_code":"33910"},{"zip_code":"56649"},{"zip_code":"02134"},{"zip_code":"99716"},{"zip_code":"79119"},{"zip_code":"11706"},{"zip_code":"21152"},{"zip_code":"95249"},{"zip_code":"40299"},{"zip_code":"42221"},{"zip_code":"36140"},{"zip_code":"60190"},{"zip_code":"78108"},{"zip_code":"92705"},{"zip_code":"99636"},{"zip_code":"58466"},{"zip_code":"21229"},{"zip_code":"49008"},{"zip_code":"49788"},{"zip_code":"97032"},{"zip_code":"95922"},{"zip_code":"55418"},{"zip_code":"15748"},{"zip_code":"13762"},{"zip_code":"52330"},{"zip_code":"77025"},{"zip_code":"22657"},{"zip_code":"48622"},{"zip_code":"60548"},{"zip_code":"30909"},{"zip_code":"38069"},{"zip_code":"63473"},{"zip_code":"53816"},{"zip_code":"71438"},{"zip_code":"43721"},{"zip_code":"35552"},{"zip_code":"90224"},{"zip_code":"05441"},{"zip_code":"40119"},{"zip_code":"28741"},{"zip_code":"46974"},{"zip_code":"21035"},{"zip_code":"48427"},{"zip_code":"02129"},{"zip_code":"13843"},{"zip_code":"45121"},{"zip_code":"55432"},{"zip_code":"32648"},{"zip_code":"37872"},{"zip_code":"35775"},{"zip_code":"18407"},{"zip_code":"73763"},{"zip_code":"35204"},{"zip_code":"76655"},{"zip_code":"14519"},{"zip_code":"56050"},{"zip_code":"33870"},{"zip_code":"61377"},{"zip_code":"34206"},{"zip_code":"21557"},{"zip_code":"42743"},{"zip_code":"41774"},{"zip_code":"32413"},{"zip_code":"11507"},{"zip_code":"05072"},{"zip_code":"28584"},{"zip_code":"02562"},{"zip_code":"24902"},{"zip_code":"75483"},{"zip_code":"73438"},{"zip_code":"30548"},{"zip_code":"21104"},{"zip_code":"39058"},{"zip_code":"17582"},{"zip_code":"93502"},{"zip_code":"32948"},{"zip_code":"87042"},{"zip_code":"30530"},{"zip_code":"70381"},{"zip_code":"26447"},{"zip_code":"56688"},{"zip_code":"38027"},{"zip_code":"57724"},{"zip_code":"22180"},{"zip_code":"50634"},{"zip_code":"04349"},{"zip_code":"74422"},{"zip_code":"58486"},{"zip_code":"28671"},{"zip_code":"91732"},{"zip_code":"01784"},{"zip_code":"15127"},{"zip_code":"67054"},{"zip_code":"41262"},{"zip_code":"00921"},{"zip_code":"23505"},{"zip_code":"67871"},{"zip_code":"39078"},{"zip_code":"02650"},{"zip_code":"58448"},{"zip_code":"13327"},{"zip_code":"12019"},{"zip_code":"16260"},{"zip_code":"33031"},{"zip_code":"48529"},{"zip_code":"75154"},{"zip_code":"25686"},{"zip_code":"45177"},{"zip_code":"58277"},{"zip_code":"57469"},{"zip_code":"32011"},{"zip_code":"21131"},{"zip_code":"87825"},{"zip_code":"35491"},{"zip_code":"54552"},{"zip_code":"51245"},{"zip_code":"98335"},{"zip_code":"46407"}]
     */

    private String msg;
    private String status;
    private List<DataBean> data;

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
         * zip_code : J3B 6E6
         */

        private String zip_code;

        public String getZip_code() {
            return zip_code;
        }

        public void setZip_code(String zip_code) {
            this.zip_code = zip_code;
        }
    }
}
