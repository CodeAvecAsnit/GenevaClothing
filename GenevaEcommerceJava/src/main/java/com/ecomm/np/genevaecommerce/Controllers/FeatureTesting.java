package com.ecomm.np.genevaecommerce.Controllers;

import com.ecomm.np.genevaecommerce.Enumerations.Role;
import com.ecomm.np.genevaecommerce.Models.RoleTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeatureTesting {
    public static void main(String[] args) {

        List<RoleTable> roleTables = new ArrayList<>();

        RoleTable roleTable = new RoleTable();
        roleTable.setRole_Id(1);
        roleTable.setRole(Role.USER);
        roleTables.add(roleTable);  // USER

        RoleTable roleTable2 = new RoleTable();
        roleTable2.setRole_Id(2);
        roleTable2.setRole(Role.ADMIN);
        roleTables.add(roleTable2);  // ADMIN

        String role = "ADMIN";
        Role r = Role.valueOf(role);

        Optional<RoleTable> optional = roleTables.stream()
                .filter(rt -> rt.getRole().equals(r))
                .findFirst();

        if (optional.isPresent()) {
            RoleTable roleTable1 = optional.get();
            System.out.println(roleTable1.getRole_Id());
        }
    }
}
