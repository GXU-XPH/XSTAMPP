package xstampp.astpa.model;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class AbstractNumberedEntry implements NumberedEntry {


  @XmlAttribute
  private int number;
  
  public AbstractNumberedEntry() {
    this.number = -1;
  }

  @Override
  public boolean setNumber(int i) {
    if(this.number != i) {
      this.number = i;
      return true;
    }
    return false;
  }

  @Override
  public int getNumber() {
    return this.number;
  }

}