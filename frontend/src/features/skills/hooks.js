import { useQuery } from "@tanstack/react-query";
import { getCategories, getRoles, getRoleByName } from "./api";
import { QUERY_KEYS } from "../../constants/queryKeys";

export const useCategories = () => {
  return useQuery({
    queryKey: QUERY_KEYS.CATEGORIES,
    queryFn: () => getCategories().then((res) => res.data),
  });
};

export const useRoles = (category) => {
  return useQuery({
    queryKey: QUERY_KEYS.ROLES(category),
    queryFn: () => getRoles(category).then((res) => res.data),
    enabled: !!category, // only fetch when category is selected
  });
};

export const useRoleByName = (roleName) => {
  return useQuery({
    queryKey: QUERY_KEYS.ROLE_BY_NAME(roleName),
    queryFn: () => getRoleByName(roleName).then((res) => res.data),
    enabled: !!roleName,
  });
};