package com.ecomm.np.genevaecommerce.service.admin.impl;

class Solution {
    public String processStr(String s) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < s.length(); ++i){
            char curr = s.charAt(i);
            switch(curr){
                case '#'-> sb.append(sb.toString());
                case '*'-> (sb.length()!=0)? sb.setLength(sb.length()-1):;
                case '%'-> sb.reverse();
                default -> sb.append(curr);
            }
        }
        return sb.toString();
    }
}