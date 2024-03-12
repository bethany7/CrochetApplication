package org.batah;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.batah.model.Pattern;

public class SerializationUtil {

//   deserialize to Object from given file
  public static Object deserialize(String fileName) throws IOException,
      ClassNotFoundException {
    FileInputStream fis = new FileInputStream(fileName);
    ObjectInputStream ois = new ObjectInputStream(fis);
    Object obj = ois.readObject();
    ois.close();
    return obj;
  }

  // serialize the given object and save it to file
  public static void serialize(Object obj, String fileName)
      throws IOException {
    FileOutputStream fos = new FileOutputStream(fileName);
    ObjectOutputStream oos = new ObjectOutputStream(fos);
    oos.writeObject(obj);

    fos.close();
  }

    // deserialize to Object from given file
//    public static Pattern deserialize(String fileName) throws IOException,
//        ClassNotFoundException {
//      FileInputStream fis = new FileInputStream(fileName);
//      ObjectInputStream ois = new ObjectInputStream(fis);
//      Object obj = ois.readObject();
//      ois.close();
//      if (obj instanceof Pattern) {
//        return (Pattern) obj;
//      } else {
//        throw new IllegalArgumentException("The object in the file is not a Pattern");
//      }
//    }
//
//    // serialize the given object and save it to file
//    public static void serialize(Pattern pattern, String fileName)
//        throws IOException {
//      FileOutputStream fos = new FileOutputStream(fileName);
//      ObjectOutputStream oos = new ObjectOutputStream(fos);
//      oos.writeObject(pattern);
//      fos.close();
//    }


}