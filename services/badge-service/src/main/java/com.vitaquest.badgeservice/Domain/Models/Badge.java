package com.vitaquest.badgeservice.Domain.Models;

import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.*;
import org.bson.types.Binary;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Base64;

@Document("badges")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "uuid-char")
    private String id;
    private String name;
    private String description;
    private String activityId;
    private int bronzeTreshold;
    private int silverTreshold;
    private int goldTreshold;
    private BadgeStatus status;
    private Binary imageSet;// don't know if it must be a array or just normal.

    // Might convert to mapping function later but this functions for now.
    public JSONObject toJson(){
        JSONObject output = new JSONObject();
        output.put("id", this.id);
        output.put("name", this.name);
        output.put("description", this.description);
        output.put("activityId", this.activityId);
        output.put("bronzeTreshold", this.bronzeTreshold);
        output.put("silverTreshold", this.silverTreshold);
        output.put("goldTreshold", this.goldTreshold);
        output.put("status", this.status);

        // Convert to base64 format so it is usable in front end.
        output.put("image", Base64.getEncoder().encodeToString(this.imageSet.getData()));
        return output;
    }
}
