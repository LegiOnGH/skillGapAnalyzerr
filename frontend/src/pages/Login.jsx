import { useState } from "react";
import { Link } from "react-router-dom";
import AuthLayout from "../layouts/AuthLayout";
import { useLogin } from "../features/auth/hooks";

const Login = () => {
  const [form, setForm] = useState({ userName: "", password: "" });
  const [errors, setErrors] = useState({ fields: {}, general: "" });
  const { mutate: login, isPending } = useLogin(setErrors);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    // clear field error when user starts typing
    setErrors((prev) => ({
      ...prev,
      fields: { ...prev.fields, [e.target.name]: "" },
      general: "",
    }));
  };

  const handleSubmit = (e) => {
  e.preventDefault();

  // frontend validation before hitting backend
  const newErrors = { fields: {}, general: "" };

  if (!form.userName.trim()) {
    newErrors.general = "Please enter your username.";
  } else if (!form.password.trim()) {
    newErrors.general = "Please enter your password.";
  } else if (form.password.length < 6) {
    newErrors.general = "Password must be at least 6 characters.";
  }

  if (newErrors.general) {
    setErrors(newErrors);
    return;
  }

  setForm({ userName: "", password: "" });
  login(form);
};

  return (
    <AuthLayout>
      <h2 className="text-2xl font-bold text-gray-800 mb-6">Welcome back</h2>

      {errors.general && (
        <div className="bg-red-50 text-red-600 text-sm rounded-lg p-3 mb-4">
          {errors.general}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Username
          </label>
          <input
            type="text"
            name="userName"
            value={form.userName}
            onChange={handleChange}
            placeholder="Enter username"
            className={`w-full border rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
              errors.fields.userName ? "border-red-400" : "border-gray-300"
            }`}
            required
          />
          {errors.fields.userName && (
            <p className="text-red-500 text-xs mt-1">{errors.fields.userName}</p>
          )}
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Password
          </label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
            placeholder="Enter password"
            className={`w-full border rounded-lg px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 ${
              errors.fields.password ? "border-red-400" : "border-gray-300"
            }`}
            required
          />
          {errors.fields.password && (
            <p className="text-red-500 text-xs mt-1">{errors.fields.password}</p>
          )}
        </div>

        <button
          type="submit"
          disabled={isPending}
          className="w-full bg-indigo-600 hover:bg-indigo-700 disabled:bg-indigo-400 text-white font-semibold rounded-lg py-2.5 text-sm transition-colors"
        >
          {isPending ? "Signing in..." : "Sign in"}
        </button>
      </form>

      <p className="text-center text-sm text-gray-500 mt-6">
        Don't have an account?{" "}
        <Link to="/signup" className="text-indigo-600 font-medium hover:underline">
          Sign up
        </Link>
      </p>
    </AuthLayout>
  );
};

export default Login;