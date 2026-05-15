import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { getMe, updateMe } from "./api";

export const useMe = () => {
  return useQuery({
    queryKey: ["me"],
    queryFn: () => getMe().then((res) => res.data),
  });
};

export const useUpdateMe = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: updateMe,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["me"] }),
  });
};