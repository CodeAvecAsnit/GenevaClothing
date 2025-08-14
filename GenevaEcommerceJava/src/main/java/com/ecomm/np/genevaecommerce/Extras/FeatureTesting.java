package com.ecomm.np.genevaecommerce.Extras;

import com.ecomm.np.genevaecommerce.Models.BestCollection;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class FeatureTesting {
    public static void main(String[] args) throws IOException {
//
//        List<RoleTable> roleTables = new ArrayList<>();
//
//        RoleTable roleTable = new RoleTable();
//        roleTable.setRole_Id(1);
//        roleTable.setRole(Role.USER);
//        roleTables.add(roleTable);  // USER
//
//        RoleTable roleTable2 = new RoleTable();
//        roleTable2.setRole_Id(2);
//        roleTable2.setRole(Role.ADMIN);
//        roleTables.add(roleTable2);  // ADMIN
//
//        String role = "ADMIN";
//        Role r = Role.valueOf(role);
//
//        Optional<RoleTable> optional = roleTables.stream()
//                .filter(rt -> rt.getRole().equals(r))
//                .findFirst();
//
//        if (optional.isPresent()) {
//            RoleTable roleTable1 = optional.get();
//            System.out.println(roleTable1.getRole_Id());

        String filePath = "collection.json";
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        BestCollection collection = objectMapper.readValue(file, BestCollection.class);
        if (collection.getName() != null) {
            System.out.println(collection.toString());
        } else System.out.println("cannot map");


        BestCollection collect = new BestCollection();
        collect.setDescription("ola");
        collect.setId(2);
        collect.setName("dream");

        objectMapper.writeValue(file,collect);

    }

}
