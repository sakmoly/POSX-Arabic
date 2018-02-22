/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tecnooc.desktop.app.posx.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jomit
 */
public class UserPermissionsDto {
    public static int DISCARD_RECEIPT_IN_PROGRESS = 1;
    public static int HOLD_UNHOLD_RECEIPT = 2;
    public static int GIVE_GLOBAL_DISCOUNT = 3;
    public static int ITEMS_ADD = 4;
    public static int ITEMS_DELETE_LINE_ITEM = 5;
    public static int ITEMS_RETURNS = 6;
    public static int ITEMS__VOIDS = 7;
    public static int ITEMS_CHANGE_PRICE = 8;
    public static int ITEMS__DISCOUNT = 9;
    public static int DISCOUNT_RECEIPT_PRICE_BELOW_INVENTORY_COST = 10;
    public static int ACCESS_CUSTOMERS = 11;
    public static int ACCESS_TENDER_CASH = 12;
    public static int ACCESS_TENDER_BANK_CARD = 13;
    public static int ACCESS_TENDER_CREDIT_SALE = 14;
    public static int CREDIT_SALE_EXCEED_CREDIT_LIMIT = 15;
    public static int ISSUE_STORE_CREDIT = 16;
    public static int REDEEM_STORE_CREDIT = 17;
    public static int XOUT_ACCESS_XOUT = 18;
    public static int ZOUT_ACCESS_REGISTER_OPEN_CLOSE = 19;
    public static int DELETE_TENDER = 20;
    
    private List<Integer> permissions;

    public UserPermissionsDto() {
        permissions = new ArrayList<>();
    }
    
    public boolean hasPermission(int permission) {
        return permissions.contains(permission);
    }
    
    public void addPermission(int permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
        }
    }
}
