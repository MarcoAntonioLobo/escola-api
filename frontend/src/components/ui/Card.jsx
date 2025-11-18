export function Card({ children, className = "", ...props }) {
  return (
    <div className={`bg-gray-800 shadow-lg rounded-2xl p-4 relative ${className}`} {...props}>
      {children}
    </div>
  );
}

export function CardContent({ children, className = "", ...props }) {
  return (
    <div className={`p-2 ${className}`} {...props}>
      {children}
    </div>
  );
}
