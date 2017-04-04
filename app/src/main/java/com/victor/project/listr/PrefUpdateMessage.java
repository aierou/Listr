package com.victor.project.listr;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties class PrefUpdateMessage {
    private boolean EditButton;
    private boolean AddButton;
    private boolean DeleteButton;
    private boolean PrivateButton;
    private boolean PublicButton;
    private boolean YesButton;
    private boolean NoButton;
    private double latitude;
    private double longitude;

    PrefUpdateMessage(boolean EditButton, boolean AddButton, boolean DeleteButton, boolean PrivateButton
            , boolean PublicButton, boolean YesButton, boolean NoButton, double latitude, double longitude) {
        this.EditButton = EditButton;
        this.AddButton = AddButton;
        this.DeleteButton = DeleteButton;
        this.PrivateButton = PrivateButton;
        this.PublicButton = PublicButton;
        this.YesButton = YesButton;
        this.NoButton = NoButton;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Exclude
    Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("EditButton", EditButton);
        result.put("AddButton", AddButton);
        result.put("DeleteButton", DeleteButton);
        result.put("PrivateButton", PrivateButton);
        result.put("PublicButton", PublicButton);
        result.put("YesButton", YesButton);
        result.put("NoButton", NoButton);
        result.put("Latitude", latitude);
        result.put("Longitude", longitude);
        return result;
    }

    @Override public String toString() {
        return "Message{" +
                ", EditButton=" + EditButton + '\'' +
                ", AddButton=" + AddButton + '\'' +
                ", DeleteButton=" + DeleteButton + '\'' +
                ", PrivateButton=" + PrivateButton + '\'' +
                ", PublicButton=" + PublicButton + '\'' +
                ", YesButton=" + YesButton + '\'' +
                ", NoButton=" + NoButton + '\'' +
                ", Latitude=" + latitude + '\'' +
                ", Longitude=" + longitude +
                '}';
    }
}