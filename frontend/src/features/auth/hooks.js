import { useMutation } from "@tanstack/react-query";
import { login, signup } from "./api";
import useAuthStore from "../../store/authStore";
import { useNavigate } from "react-router-dom";

// returns { userName: "...", email: "...", password: "..." }
const parseFieldErrors = (error) => {
  const msg = error.response?.data?.message || "";
  const fieldErrors = {};

  if (msg.startsWith("Validation failed:")) {
    const inner = msg.replace("Validation failed:", "").trim();
    const cleaned = inner.replace(/[{}]/g, "");
    cleaned.split(",").forEach((part) => {
      const [key, ...rest] = part.split("=");
      if (key && rest.length) {
        fieldErrors[key.trim()] = rest.join("=").trim();
      }
    });
    return { fieldErrors, generalError: "" };
  }

  return { fieldErrors: {}, generalError: msg || "Something went wrong." };
};

export const useLogin = (setErrors) => {
  const { login: storeLogin } = useAuthStore();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: login,
    onSuccess: (response) => {
      const { accessToken, role, userId } = response.data;
      storeLogin({ accessToken, role, userId });
      navigate("/dashboard");
    },
    onError: (error) => {
      // always generic for login — never reveal which field is wrong
      const msg = error.response?.data?.message || "Login failed. Please try again.";
      setErrors({ fields: {}, general: msg });
    },
  });
};

export const useSignup = (setErrors) => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: signup,
    onSuccess: () => {
      navigate("/login");
    },
    onError: (error) => {
      const { fieldErrors, generalError } = parseFieldErrors(error);
      setErrors({ fields: fieldErrors, general: generalError });
    },
  });
};