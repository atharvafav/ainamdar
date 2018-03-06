clc;
clear all;
close all;
load('Inamdar.mat'); %Loading the signal
x=signal; %Assign the signal to x
t_s=1*10^-6; %Sampling time of the signal
s_f=1/t_s;
y=fft(x); %Taking fourier transform
l=length(y)-1;
df=s_f/l;
frequency=0:df:s_f; %Range of frequencies
figure(1);
subplot(3,1,1); plot(x,'g'); %Plotting the signal
title('Original signal');xlabel('t[s]');ylabel('x(t)');grid on;
axis([0 length(x) -50 50]);
subplot(3,1,2);plot(frequency,abs(y),'b'); %Plotting the fourier transform of the signal
title('Fourier transform of the signal');xlabel('f');ylabel('X(f)');grid on;
xp=zeros(1,length(y)); %Taking an array of zeros to store the frequencies
temp=0;
window_size=10000; %Size of the window
for i=1:window_size:length(x) 
    temp=i+window_size; 
    window=zeros(1,length(x)); 
    positions=i:temp;
    window(positions)=1; %Rectangular window
    x1=x.*window; %Multiplying window and the signal
    y1=fft(x1); %Taking fourier transform of the windowed signal
    X = frequency; %Range of frequencies    
    Y = abs(y1); %Absolute value of the fourier transform
    index = find(Y==max(Y)); %Finding the maximum of the fft of the window
    X_point=min(X(index)); %Finding the corresponding x co-ordinate which is the frequency
    X_point1=X_point*10^-3; %Converting the value to Kilo Hertz
    xp(positions)=X_point1; %Giving the same value to the entire window
  if(temp>=length(x)) 
    break % Break the loop if the window exceeds the signal
  end
end
time=1:length(x);
subplot(3,1,3);
plot(time,xp,'r'); %Plot the range of frequencies 
title('Output of frequency tracker');ylabel('f (KHz)');xlabel('t[s]');
axis([0 length(x) 0 30]);grid on;