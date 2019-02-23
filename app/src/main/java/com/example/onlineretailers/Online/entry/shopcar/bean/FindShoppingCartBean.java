package com.example.onlineretailers.Online.entry.shopcar.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class FindShoppingCartBean implements Parcelable {
    private String message;
    private String status;
    private List<ResultBean> result;
    private final String SUCCESS_STATUS="0000";

    protected FindShoppingCartBean(Parcel in) {
        message = in.readString();
        status = in.readString();
    }

    public static final Creator<FindShoppingCartBean> CREATOR = new Creator<FindShoppingCartBean>() {
        @Override
        public FindShoppingCartBean createFromParcel(Parcel in) {
            return new FindShoppingCartBean(in);
        }

        @Override
        public FindShoppingCartBean[] newArray(int size) {
            return new FindShoppingCartBean[size];
        }
    };

    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeString(status);
        dest.writeString(SUCCESS_STATUS);
    }

    public static class ResultBean implements Parcelable{


        private int commodityId;
        private String commodityName;
        private int count;
        private String pic;
        private double price;
        private boolean isChecked = false;

        protected ResultBean(Parcel in) {
            commodityId = in.readInt();
            commodityName = in.readString();
            count = in.readInt();
            pic = in.readString();
            price = in.readDouble();
            isChecked = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(commodityId);
            dest.writeString(commodityName);
            dest.writeInt(count);
            dest.writeString(pic);
            dest.writeDouble(price);
            dest.writeByte((byte) (isChecked ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ResultBean> CREATOR = new Creator<ResultBean>() {
            @Override
            public ResultBean createFromParcel(Parcel in) {
                return new ResultBean(in);
            }

            @Override
            public ResultBean[] newArray(int size) {
                return new ResultBean[size];
            }
        };

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public int getCommodityId() {
            return commodityId;
        }

        public void setCommodityId(int commodityId) {
            this.commodityId = commodityId;
        }

        public String getCommodityName() {
            return commodityName;
        }

        public void setCommodityName(String commodityName) {
            this.commodityName = commodityName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}
