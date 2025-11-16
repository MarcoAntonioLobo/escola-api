export function Button({ children, className = "", variant = "primary", ...props }) {
  const baseClasses = "px-4 py-2 rounded-xl font-medium transition shadow-md";

  const variants = {
    primary: "bg-gray-700 text-gray-100 hover:bg-gray-600",
    secondary: "bg-gray-600 text-gray-100 hover:bg-gray-500",
  };

  return (
    <button className={`${baseClasses} ${variants[variant] || variants.primary} ${className}`} {...props}>
      {children}
    </button>
  );
}
