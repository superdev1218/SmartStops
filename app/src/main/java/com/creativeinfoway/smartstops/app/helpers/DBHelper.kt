package com.creativeinfoway.smartstops.app.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.creativeinfoway.smartstops.app.android.navigation.ui.v5.GetAllSubCategoryWaypoint
import com.creativeinfoway.smartstops.app.ui.models.CoordinateLatLong
import com.creativeinfoway.smartstops.app.ui.models.GetAllCategories
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mapbox.geojson.Point
import java.util.*

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, 1)  {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $CATEGORY_TAB ($_ID INTEGER PRIMARY KEY AUTOINCREMENT,$CAT_ID TEXT,$NAME TEXT,$IMAGE TEXT,$IMAGE_NAME TEXT,$TOTAL_SUB_CAT TEXT)")
        db?.execSQL("CREATE TABLE $SUBCATEGORY_TAB (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$ID TEXT, " +
                "$CAT_ID TEXT, " +
                "$SUB_CAT_ID TEXT, " +
                "$LATITUDE TEXT, " +
                "$LONGITUDE TEXT, " +
                "$NAME TEXT, " +
                "$LATITUDE_DECIMAL TEXT, " +
                "$LONGITUDE_DECIMAL TEXT, " +
                "$WAYPOINT_ID TEXT, " +
                "$ADDRESS TEXT, " +
                "$ADDITIONAL_INFO TEXT, " +
                "$COUNTRY TEXT, " +
                "$EMAIL TEXT, " +
                "$PHONE_NUMBER TEXT, " +
                "$WAYPOINT_ICON_IMAGE TEXT," +
                "$WAYPOINT_IMAGE TEXT, " +
                "$SUBCAT_ARRAY TEXT)")
        db?.execSQL("CREATE TABLE $COORDINATES ($_ID  INTEGER PRIMARY KEY AUTOINCREMENT,$MAP_NAME TEXT,$COORDINATE_ARRAY TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun getCategories(): MutableList<GetAllCategories.DataBean> {
        val db = this.readableDatabase
        val categories = mutableListOf<GetAllCategories.DataBean>()
        val cursor = db.query(CATEGORY_TAB, null, null, null, null, null, null)
        cursor?.use {
            if(it.moveToFirst()){
                do {
                    categories.add(GetAllCategories.DataBean().apply {
                        id = it.getString(it.getColumnIndexOrThrow(CAT_ID))
                        name = it.getString(it.getColumnIndexOrThrow(NAME))
                        image = it.getString(it.getColumnIndexOrThrow(IMAGE))
                        image_name = it.getString(it.getColumnIndexOrThrow(IMAGE_NAME))
                        total_sub_cat = it.getString(it.getColumnIndexOrThrow(TOTAL_SUB_CAT))
                    })
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return categories
    }

    fun saveCategories(categories: MutableList<GetAllCategories.DataBean>, page: Int) {
        val db = this.writableDatabase
        if(page == 0) {
            db.delete(CATEGORY_TAB, null, null)
        }
        categories.forEach{
            ContentValues().apply {
                put(CAT_ID, it.id)
                put(NAME, it.name)
                put(IMAGE, it.image)
                put(IMAGE_NAME, it.image_name)
                put(TOTAL_SUB_CAT, it.total_sub_cat)
            }.also { contentValues ->
                db.insert(CATEGORY_TAB, null, contentValues)
            }
        }
        db.close()
    }

    fun getSubCategory(catId: String): MutableList<GetAllSubCategoryWaypoint.DataBean> {
        val db = this.readableDatabase
        val subcategories = mutableListOf<GetAllSubCategoryWaypoint.DataBean>()
        val cursor = db.query(SUBCATEGORY_TAB, null, "$CAT_ID like $catId", null, null, null, null)
        cursor?.use {
            if(it.moveToFirst()){
                do{
                    subcategories.add(GetAllSubCategoryWaypoint.DataBean().apply {
                        id = it.getString(it.getColumnIndexOrThrow(ID))
                        cat_id = it.getString(it.getColumnIndexOrThrow(CAT_ID))
                        sub_cat_id = it.getString(it.getColumnIndexOrThrow(SUB_CAT_ID))
                        latitude = it.getString(it.getColumnIndexOrThrow(LATITUDE))
                        longitude = it.getString(it.getColumnIndexOrThrow(LONGITUDE))
                        latitude_decimal = it.getString(it.getColumnIndexOrThrow(LATITUDE_DECIMAL))
                        longitude_decimal = it.getString(it.getColumnIndexOrThrow(LONGITUDE_DECIMAL))
                        name = it.getString(it.getColumnIndexOrThrow(NAME))
                        waypoint_id = it.getString(it.getColumnIndex(WAYPOINT_ID))
                        address = it.getString(it.getColumnIndexOrThrow(ADDRESS))
                        additional_info = it.getString(it.getColumnIndexOrThrow(ADDITIONAL_INFO))
                        country = it.getString(it.getColumnIndexOrThrow(COUNTRY))
                        email = it.getString(it.getColumnIndexOrThrow(EMAIL))
                        phone_number = it.getString(it.getColumnIndexOrThrow(PHONE_NUMBER))
                        waypoint_icon_image = it.getString(it.getColumnIndexOrThrow(WAYPOINT_ICON_IMAGE))
                        waypoint_image = it.getString(it.getColumnIndexOrThrow(WAYPOINT_IMAGE))
                        val type = object : TypeToken<MutableList<GetAllSubCategoryWaypoint.DataBean.SubCatArrayBean>>() {}.type
                        sub_cat_array = Gson().fromJson(it.getString(it.getColumnIndexOrThrow(SUBCAT_ARRAY)), type)
                    })
                }while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return subcategories
    }

    fun saveSubCategory(subCategories: MutableList<GetAllSubCategoryWaypoint.DataBean>, cat_id: String, page: Int){
        val db = this.writableDatabase
        if(page == 0){
            db.delete(SUBCATEGORY_TAB, "$CAT_ID like ${cat_id}", null)
        }
        subCategories.forEach {
            ContentValues().apply {
                put(ID, it.id)
                put(CAT_ID, it.cat_id)
                put(SUB_CAT_ID, it.sub_cat_id)
                put(LATITUDE, it.latitude)
                put(LONGITUDE, it.longitude)
                put(LATITUDE_DECIMAL, it.latitude_decimal)
                put(LONGITUDE_DECIMAL, it.longitude_decimal)
                put(NAME, it.name)
                put(WAYPOINT_ID, it.waypoint_id)
                put(ADDRESS, it.address)
                put(ADDITIONAL_INFO, it.additional_info)
                put(COUNTRY, it.country)
                put(EMAIL, it.email)
                put(PHONE_NUMBER, it.phone_number)
                put(WAYPOINT_ICON_IMAGE, it.waypoint_icon_image)
                put(WAYPOINT_IMAGE, it.waypoint_image)
                put(SUBCAT_ARRAY, Gson().toJson(it.sub_cat_array))
            }.also { contentValues ->
                val colId = db.insert(SUBCATEGORY_TAB, null, contentValues)
                println("Col ID --> $colId")
            }
        }
        db.close()
    }

    fun getCoordinates(mapName: String): MutableList<Point> {
        val db = this.readableDatabase
        val coordinateLatLongs = mutableListOf<CoordinateLatLong>()
        val cursor = db.query(COORDINATES, null, "$MAP_NAME like '$mapName'", null, null, null, null)
        cursor?.use {
            if(it.moveToFirst()){
                val coordinate = it.getString(it.getColumnIndexOrThrow(COORDINATE_ARRAY))
                val type = object : TypeToken<MutableList<CoordinateLatLong>>(){}.type
                val coordinateLatLongArray = Gson().fromJson<MutableList<CoordinateLatLong>>(coordinate, type)
                coordinateLatLongArray?.let {
                    coordinateLatLongs.addAll(it)
                }
            }
        }
        cursor?.close()
        db.close()
        val points = mutableListOf<Point>()
        coordinateLatLongs.forEach {
            points.add(Point.fromLngLat(it.longitude ?: 0.0,it.latitude ?: 0.0))
        }
        return points
    }

    fun saveCoordinates(mapName: String, points: MutableList<Point>?){
        val coordinateLatLong = mutableListOf<CoordinateLatLong>()
        points?.forEach {
            coordinateLatLong.add(CoordinateLatLong(it.latitude(), it.longitude()))
        }
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(MAP_NAME, mapName)
        cv.put(COORDINATE_ARRAY, Gson().toJson(coordinateLatLong))
        db.insert(COORDINATES, null, cv)
        db.close()
    }

    fun deleteCoordinate(mapName: String){
        val db = this.writableDatabase
        db.delete(COORDINATES, "$MAP_NAME like '$mapName'", null)
        db.close()
    }

    companion object {
        private const val DB_NAME = "SmartStops"

        private const val CATEGORY_TAB = "categories"
        private const val SUBCATEGORY_TAB = "sub_category"
        private const val COORDINATES = "coordinates"

        private const val _ID = "_id"
        private const val NAME = "name"
        private const val IMAGE = "image"
        private const val IMAGE_NAME = "image_name"
        private const val TOTAL_SUB_CAT = "total_sub_cat"

        private const val COL_ID = "_id"
        private const val ID = "id"
        private const val CAT_ID = "cat_id"
        private const val SUB_CAT_ID = "sub_cat_id"
        private const val LATITUDE = "latitude"
        private const val LONGITUDE = "longitude"
        private const val LATITUDE_DECIMAL = "latitude_decimal"
        private const val LONGITUDE_DECIMAL = "longitude_decimal"
        private const val WAYPOINT_ID = "waypoint_id"
        private const val ADDRESS = "address"
        private const val ADDITIONAL_INFO = "additional_info"
        private const val COUNTRY = "country"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phone_number"
        private const val WAYPOINT_ICON_IMAGE = "waypoint_icon_image"
        private const val WAYPOINT_IMAGE = "waypoint_image"
        private const val SUBCAT_ARRAY = "sub_cat_array"

        private const val MAP_NAME = "map_name"
        private const val COORDINATE_ARRAY = "coordinate_array"
    }
}