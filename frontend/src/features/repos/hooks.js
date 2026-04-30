import { useMutation } from "@tanstack/react-query";
import { recommendRepos } from "./api";

export const useRepoRecommendation = () => {
  return useMutation({
    mutationFn: recommendRepos,
  });
};