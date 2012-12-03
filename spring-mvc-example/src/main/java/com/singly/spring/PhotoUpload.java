package com.singly.spring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

public class PhotoUpload
  implements Serializable, Cloneable {

  private LinkedHashMap<String, MultipartFile> photos = new LinkedHashMap<String, MultipartFile>();

  public PhotoUpload() {

  }

  public LinkedHashMap<String, MultipartFile> getPhotos() {
    return photos;
  }

  public void setPhotos(LinkedHashMap<String, MultipartFile> photos) {
    this.photos = photos;
  }

  public MultipartFile[] toArray() {

    List<MultipartFile> photoList = new ArrayList<MultipartFile>();
    Iterator<MultipartFile> photoIt = photos.values().iterator();
    while (photoIt.hasNext()) {
      MultipartFile current = photoIt.next();
      long sizeInBytes = current.getSize();
      if (sizeInBytes > 0) {
        photoList.add(current);
      }
    }

    MultipartFile[] photos = new MultipartFile[photoList.size()];
    photos = (MultipartFile[])photoList.toArray(photos);
    return photos;
  }

  public Object clone()
    throws CloneNotSupportedException {
    return super.clone();
  }

  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
