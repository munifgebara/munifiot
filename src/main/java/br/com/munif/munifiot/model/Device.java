package br.com.munif.munifiot.model;

import br.com.munif.framework.core.MultiTenancySuperEntity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Device extends MultiTenancySuperEntity{
    
    private String deviceId;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastStatusMoment;
    private String lastStatus;
    private String nextCommand;

    public Device() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Date getLastStatusMoment() {
        return lastStatusMoment;
    }

    public void setLastStatusMoment(Date lastStatusMoment) {
        this.lastStatusMoment = lastStatusMoment;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getNextCommand() {
        return nextCommand;
    }

    public void setNextCommand(String nextCommand) {
        this.nextCommand = nextCommand;
    }

    @Override
    public String toString() {
        return "Device{" + "deviceId=" + deviceId + ", lastStatusMoment=" + lastStatusMoment + ", lastStatus=" + lastStatus + ", nextCommand=" + nextCommand + '}';
    }
    
}
