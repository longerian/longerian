package me.longerian.abc.flatbuffer;

import com.google.flatbuffers.FlatBufferBuilder;
import com.sun.tools.javac.util.Assert;
import me.longerian.fbs.FriendshipStatus;
import me.longerian.fbs.Person;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by longerian on 2017/5/30.
 */
public class FlatBufferTest {

    public static void main(String[] args) throws JSONException {
        ByteBuffer byteBuffer = createPerson();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Person person = Person.getRootAsPerson(byteBuffer);
//            Assert.check(person.name().equals("John"));
//            Assert.check(person.spouse().name().equals("Mary"));
        }
        long end = System.currentTimeMillis();
        System.out.println("parsing buffer costs " + (end - start));

        String jsonString = "{\"name\":\"John\",\"friendshipStatus\":1,\"spouse\":{\"name\":\"Mary\",\"friendshipStatus\":1,\"friends\":[]},\"friends\":[{\"name\":\"Dave\",\"friendshipStatus\":1,\"friends\":[]},{\"name\":\"Tom\",\"friendshipStatus\":1,\"friends\":[]}]}";
        JSONObject jsonObject = new JSONObject(jsonString);
        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            JsonPerson person = createPersonFromJson(jsonObject);
//            Assert.check(person.name.equals("John"));
//            Assert.check(person.spouse.name.equals("Mary"));
        }
        end = System.currentTimeMillis();
        System.out.println("parsing json costs " + (end - start));
    }

    private static ByteBuffer createPerson() {
        FlatBufferBuilder builder = new FlatBufferBuilder(0);
        int spouseName = builder.createString("Mary");
        int spouse = Person.createPerson(builder, spouseName, FriendshipStatus.Friend, 0, 0);

        int friendDave = Person.createPerson(builder, builder.createString("Dave"),
                FriendshipStatus.Friend, 0, 0);
        int friendTom = Person.createPerson(builder, builder.createString("Tom"),
                FriendshipStatus.Friend, 0, 0);

        int name = builder.createString("John");
        int[] friendsArr = new int[]{ friendDave, friendTom };
        int friends = Person.createFriendsVector(builder, friendsArr);

        Person.startPerson(builder);
        Person.addName(builder, name);
        Person.addSpouse(builder, spouse);
        Person.addFriends(builder, friends);
        Person.addFriendshipStatus(builder, FriendshipStatus.NotFriend);

        int john = Person.endPerson(builder);
        builder.finish(john);

        return builder.dataBuffer();
    }

    private static JsonPerson createPersonFromJson(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        JsonPerson person = new JsonPerson();
        person.name = jsonObject.optString("name");
        person.friendshipStatus = jsonObject.optInt("friendshipStatus");
        person.spouse = createPersonFromJson(jsonObject.optJSONObject("spouse"));
        JSONArray friends = jsonObject.optJSONArray("friends");
        person.friends = new ArrayList<>();
        for (int i = 0, length = friends.length(); i < length; i++) {
            JsonPerson friend = createPersonFromJson(friends.optJSONObject(i));
            if (friend != null) {
                person.friends.add(friend);
            }
        }
        return person;
    }

    private static class JsonPerson {
        public String name;
        public int friendshipStatus;
        public JsonPerson spouse;
        public List<JsonPerson> friends;
    }

}
