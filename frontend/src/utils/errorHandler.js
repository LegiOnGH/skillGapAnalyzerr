export const getErrorMessage = (error) => {
  const msg = error?.response?.data?.message;
  if (!msg) return "Something went wrong. Please try again.";

  if (msg.startsWith("Validation failed:")) {
    const inner = msg.replace("Validation failed:", "").trim();
    const cleaned = inner.replace(/[{}]/g, "");
    return cleaned
      .split(",")
      .map((part) => part.split("=")[1]?.trim())
      .filter(Boolean)
      .join(" • ");
  }

  return msg;
};