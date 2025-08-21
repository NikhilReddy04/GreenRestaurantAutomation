package org.epam.devbistro.mainContext;

import org.epam.devbistro.model.ClientBookingsPayload;
import org.epam.devbistro.utils.Slot;

public class MainContext {
    ClientBookingsPayload payload;
    Slot slot;

    public ClientBookingsPayload getBookingsPayload() {
        return payload;
    }

    public void setBookingsPayload(ClientBookingsPayload payload) {
        this.payload = payload;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }
}
