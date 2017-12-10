package com.bignerdranch.android.callblocker;

/**
 * Created by brycesulin
 */

public class Blocklist {

    // Table Fields
    public long id;
    public String phoneNumber;

    public Blocklist() {

    }

    public Blocklist(final String number) {
        this.phoneNumber = number;
    }

    @Override
    public boolean equals(final Object obj) {

        // If passed object is an instance of Blocklist, then compare the phone numbers, else return false if they're not equal
        if(obj.getClass().isInstance(new Blocklist()))
        {
            // Cast the object to Blocklist
            final Blocklist bl = (Blocklist) obj;

            // Compare whether the phone numbers are same
            if(bl.phoneNumber.equalsIgnoreCase(this.phoneNumber))
                return true;
        }
        return false;
    }
}