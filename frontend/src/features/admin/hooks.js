import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  createCategory,
  createRole, deleteRole, updateRole,
  createResource, getAllResources, updateResource, deleteResource,
} from "./api";
import { QUERY_KEYS } from "../../constants/queryKeys";

// categories
export const useCreateCategory = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createCategory,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: QUERY_KEYS.CATEGORIES }),
  });
};

// roles
export const useCreateRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createRole,
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: QUERY_KEYS.ROLES(variables.category) });
    },
  });
};

export const useDeleteRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: deleteRole,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["roles"] }),
  });
};

export const useUpdateRole = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }) => updateRole(id, data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["roles"] }),
  });
};

// resources
export const useAllResources = () => {
  return useQuery({
    queryKey: ["resources"],
    queryFn: () => getAllResources().then((res) => res.data),
  });
};

export const useCreateResource = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: createResource,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["resources"] }),
  });
};

export const useUpdateResource = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }) => updateResource(id, data),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["resources"] }),
  });
};

export const useDeleteResource = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({ id, resource }) => deleteResource(id, resource),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["resources"] }),
  });
};